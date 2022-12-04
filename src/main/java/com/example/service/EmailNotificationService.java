package com.example.service;

import com.example.dto.NotificationTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private static final String EMAIL_VERIFICATION_TEMPLATE = "verification.html";
    private static final String PASSWORD_RESET_TEMPLATE = "passwordReset.html";

    @Value("${frontend.server.host}")
    private String frontendServerHost;

    @Value("${frontend.endpoint.email-verification}")
    private String emailVerificationEndpoint;

    @Value("${frontend.endpoint.password-reset-confirmation}")
    private String passwordResetConfirmationEndpoint;

    private final SpringTemplateEngine templateEngine;

    private final EmailNotificationSender emailNotificationSender;

    public void sendEmailVerification(String emailTo, String token) {
        String subject = "Verify your email";
        String confirmationUrl = frontendServerHost + emailVerificationEndpoint + "?token=" + token;

        Context context = new Context(Locale.ENGLISH);
        context.setVariable("confirmationUrl", confirmationUrl);

        String body = templateEngine.process(EMAIL_VERIFICATION_TEMPLATE, context);

        NotificationTemplate template = new NotificationTemplate();
        template.addMailTo(emailTo);
        template.setEmailSubject(subject);
        template.setEmailBody(body);

        emailNotificationSender.sendEmailNotification(template);
    }

    public void sendPasswordResetEmail(String emailTo, String token) {
        String subject = "Password reset email";
        String confirmationUrl = frontendServerHost + passwordResetConfirmationEndpoint + "?token=" + token;

        Context context = new Context(Locale.ENGLISH);
        context.setVariable("confirmationUrl", confirmationUrl);

        String body = templateEngine.process(PASSWORD_RESET_TEMPLATE, context);

        NotificationTemplate template = new NotificationTemplate();
        template.addMailTo(emailTo);
        template.setEmailSubject(subject);
        template.setEmailBody(body);

        emailNotificationSender.sendEmailNotification(template);
    }
}
