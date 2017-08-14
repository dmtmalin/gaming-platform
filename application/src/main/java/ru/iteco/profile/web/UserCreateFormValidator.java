package ru.iteco.profile.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.iteco.profile.User;
import ru.iteco.profile.UserService;

@Component
public class UserCreateFormValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserCreateFormValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserCreateForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserCreateForm form = (UserCreateForm) target;
        validatePasswords(errors, form);
        validateEmail(errors, form);
    }

    private void validatePasswords(Errors errors, UserCreateForm form) {
        if (!form.getPassword().equals(form.getPasswordRepeated())) {
            errors.reject("error.password", "Пароли не совпадают");
        }
    }

    private void validateEmail(Errors errors, UserCreateForm form) {
        String email = form.getEmail();
        User user = userService.getUserByEmail(email);
        if (user != null) {
            errors.reject("error.email", String.format("Пользователь с e-mail %s уже существует", email));
        }
    }
}
