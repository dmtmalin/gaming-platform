package ru.iteco.notification;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import ru.iteco.profile.User;
import ru.iteco.profile.UserService;
import ru.iteco.property.BaseProperties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    private Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final JavaMailSender javaMailSender;
    private final BaseProperties baseProperties;
    private final UserService userService;
    private final Configuration freeMarkerConfiguration;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public NotificationServiceImpl(JavaMailSender javaMailSender,
                                   UserService userService,
                                   BaseProperties baseProperties,
                                   FreeMarkerAutoConfiguration.FreeMarkerWebConfiguration freemarkerConfiguration,
                                   ApplicationEventPublisher publisher){
        this.javaMailSender = javaMailSender;
        this.userService = userService;
        this.baseProperties = baseProperties;
        this.freeMarkerConfiguration = freemarkerConfiguration.freeMarkerConfigurer().getConfiguration();
        this.publisher = publisher;
    }

    @Async
    @Override
    public void sendActivateUserUrl(User user) {
        logger.info("Sending email activation user...");
        try {
            String baseUrl = baseProperties.getBaseUrl();
            String activationToken = userService.generateToken(user);
            String activationUrl = baseUrl + "activation?token=" + activationToken;
            Map model = new HashMap<String, String>() {{
                put("activationUrl", activationUrl);
            }};
            String html = composeHtml("notification/email_activation", model);
            sendEmail(user.getEmail(), "Активация учетной записи", html);
            publishEvent(NotificationType.ACTIVATE, user);
        } catch (Exception e) {
            logger.error("Can't send activation email. " + e.getMessage(), e);
        }
        logger.info("Email sent activation");
    }

    @Async
    @Override
    public void sendRestoreUserUrl(User user) {
        logger.info("Sending restore user password...");
        try {
            String baseUrl = baseProperties.getBaseUrl();
            String restorePasswordToken = userService.generateToken(user);
            String restorePasswordUrl = baseUrl + "restore_password?token=" + restorePasswordToken;
            Map model = new HashMap<String, String>() {{
                put("restorePasswordUrl", restorePasswordUrl);
            }};
            String html = composeHtml("notification/email_restore_password", model);
            sendEmail(user.getEmail(), "Восстановление пароля", html);
            publishEvent(NotificationType.RESTORE_PASSWORD, user);
        } catch (Exception e) {
            logger.error("Can't send restore password email. " + e.getMessage(), e);
        }
        logger.info("Email sent restore password");
    }

    @Override
    public void sendEmail(String to, String subject, String html) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setSubject(subject);
        MimeMessageHelper helper;
        helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setText(html, true);
        javaMailSender.send(message);
    }

    private String composeHtml(String templateName, Map model) throws IOException, TemplateException {
        Template template = freeMarkerConfiguration.getTemplate(templateName + ".ftl");
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    }

    private void publishEvent(NotificationType type, User user) {
        NotificationEvent event = new NotificationEvent(this, type, user);
        publisher.publishEvent(event);
    }

    private void publishEvent(NotificationType type) {
        NotificationEvent event = new NotificationEvent(this, type);
        publisher.publishEvent(event);
    }
}
