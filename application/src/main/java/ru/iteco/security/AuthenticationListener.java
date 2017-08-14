package ru.iteco.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import ru.iteco.profile.User;
import ru.iteco.profile.UserService;

@Component
public class AuthenticationListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private Logger logger = LoggerFactory.getLogger(AuthenticationListener.class);

    private final UserService userService;

    @Autowired
    public AuthenticationListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(final AuthenticationSuccessEvent authenticationSuccessEvent) {
        logger.info("User is authenticated.");
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticationSuccessEvent.getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        userService.lastLogin(user);
    }
}
