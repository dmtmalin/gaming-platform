package ru.iteco.notification;

import ru.iteco.profile.User;

import javax.mail.MessagingException;

public interface NotificationService {
    void sendEmail(String to, String subject, String html) throws MessagingException;

    void sendActivateUserUrl(User user);

    void sendRestoreUserUrl(User user);
}
