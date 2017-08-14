package ru.iteco.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.iteco.profile.User;
import ru.iteco.profile.UserService;

@ControllerAdvice
public class UserDetailsController {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsController.class);

    private final UserService userService;

    public UserDetailsController(UserService userService) {
        this.userService = userService;
        logger.info("UserDetailsController is initialized");
    }

    @ModelAttribute("user")
    public User getUser() {
        return userService.currentUser();
    }
}
