package ru.iteco.profile.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.iteco.error.BadRequestException;
import ru.iteco.notification.NotificationService;
import ru.iteco.profile.User;
import ru.iteco.profile.UserService;
import ru.iteco.profile.UserToken;

import javax.servlet.ServletException;
import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;
    private final UserCreateFormValidator userCreateFormValidator;
    private final UserChangePasswordFormValidator userChangePasswordFormValidator;
    private final NotificationService notificationService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserService userService,
                          UserCreateFormValidator userCreateFormValidator,
                          UserChangePasswordFormValidator userChangePasswordFormValidator,
                          NotificationService notificationService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.userCreateFormValidator = userCreateFormValidator;
        this.userChangePasswordFormValidator = userChangePasswordFormValidator;
        this.notificationService = notificationService;
        this.authenticationManager = authenticationManager;
    }

    @InitBinder("userCreateForm")
    public void bindUserCreateForm(WebDataBinder binder) {
        binder.addValidators(userCreateFormValidator);
    }

    @InitBinder("userChangePasswordForm")
    public void bindUserChangePasswordForm(WebDataBinder binder) {
        binder.addValidators(userChangePasswordFormValidator);
    }

    @GetMapping(value = "/registration")
    public ModelAndView showUserCreate() {
        return new ModelAndView("profile/user_create", "userCreateForm", new UserCreateForm());
    }

    @PostMapping(value = "/registration")
    public String userCreate(@Valid @ModelAttribute("userCreateForm") UserCreateForm form,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "profile/user_create";
        }
        User user;
        try {
            user = userService.create(form);
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("error", "User already exist");
            return "profile/user_create";
        }
        if (user != null)
            notificationService.sendActivateUserUrl(user);
        return "profile/user_create_success";
    }

    @GetMapping(value = "/activation")
    public String activationUser(@RequestParam String token) {
        UserToken parsedToken = userService.parseToken(token);
        if (userService.tokenExpiration(parsedToken)) {
            throw new BadRequestException();
        }
        userService.activate(parsedToken);
        return "profile/user_activation_success";
    }

    @GetMapping(value = "/change_password")
    public ModelAndView showChangePassword() {
        return new ModelAndView(
                "profile/user_change_pass", "userChangePasswordForm", new UserChangePasswordForm());
    }

    @PostMapping(value = "/change_password")
    public String changePassword(Model model,
                                 @Valid @ModelAttribute("userChangePasswordForm") UserChangePasswordForm form,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "profile/user_change_pass";
        }
        userService.changePassword(form);
        model.addAttribute("user_change_pass_success", true);
        return "profile/user_change_pass";
    }

    @GetMapping(value = "/restore_password")
    public ModelAndView restorePassword(@RequestParam(required = false) String token) {
        if (token == null) {
            return new ModelAndView(
                    "profile/user_restore_pass", "userRestorePasswordForm", new UserRestorePasswordForm());
        }
        UserToken parsedToken = userService.parseToken(token);
        if (userService.tokenExpiration(parsedToken)) {
            throw new BadRequestException();
        }
        User user = userService.getUserById(parsedToken.getId());
        if (user == null) {
            throw new BadRequestException();
        }
        autoLogin(user);
        return new ModelAndView("redirect:/change_password");
    }

    @PostMapping(value = "/restore_password")
    public String sendRestorePassword(@Valid @ModelAttribute("userRestorePasswordForm") UserRestorePasswordForm form,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "profile/user_restore_pass";
        }
        String email = form.getEmail();
        User user = userService.getUserByEmail(email);
        if (user == null) {
            bindingResult.reject("error", String.format("Пользователь с Email %s не найден", email));
            return "profile/user_restore_pass";
        }
        notificationService.sendRestoreUserUrl(user);
        return "profile/user_restore_pass_sent";
    }

    @GetMapping(value = "/login")
    public String showLoginPage(Model model,
                                @RequestParam(required = false) String error,
                                @RequestParam(required = false) String logout) throws ServletException {
        model.addAttribute("error", error);
        model.addAttribute("logout", logout);
        return "profile/user_login";
    }

    @GetMapping(value = "/change")
    public ModelAndView showUserChange() {
        User user = userService.currentUser();
        if (user == null)
            throw new BadRequestException();
        return new ModelAndView("profile/user_change", "userChangeForm", new UserChangeForm(user));
    }

    @PostMapping(value = "/change")
    public String userChange(Model model,
                             @Valid @ModelAttribute("userChangeForm") UserChangeForm form,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "profile/user_change";
        }
        User user = userService.currentUser();
        if (user == null)
            throw new BadRequestException();
        user = userService.update(user, form);
        form.setAvatarUrl(user.getAvatar());
        model.addAttribute("user_change_success", true);
        return "profile/user_change";
    }

    private void autoLogin(User user) {
        String username = user.getEmail();
        String password = user.getPasswordHash();
        Authentication token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
