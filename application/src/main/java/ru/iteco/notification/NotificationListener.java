package ru.iteco.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.iteco.profile.User;
import ru.iteco.profile.UserService;


@Component
public class NotificationListener implements ApplicationListener<NotificationEvent> {

    private final UserService userService;

    @Autowired
    public NotificationListener (UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(NotificationEvent notificationEvent) {
        NotificationType type = notificationEvent.getType();
        if (type == NotificationType.ACTIVATE) {
            User user = notificationEvent.getUser();
            if (user != null) {
                user.setSendNotificationActivate(true);
                userService.update(user);
            }
        }
    }
}
