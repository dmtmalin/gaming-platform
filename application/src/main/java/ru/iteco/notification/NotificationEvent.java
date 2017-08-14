package ru.iteco.notification;

import org.springframework.context.ApplicationEvent;
import ru.iteco.profile.User;


public class NotificationEvent extends ApplicationEvent {
    private final User user;
    private final NotificationType type;

    public NotificationEvent(Object source, NotificationType type, User user) {
        super(source);
        this.user = user;
        this.type = type;
    }
    public NotificationEvent(Object source, NotificationType type) {
        super(source);
        this.user = null;
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public NotificationType getType() {
        return type;
    }
}
