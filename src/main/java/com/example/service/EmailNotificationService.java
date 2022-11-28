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

    @Value("${frontend.server.host:http://localhost:8080}")
    private String frontendServerHost;

    @Value("${endpoint.email-verification:/api/auth/email-verification}")
    private String emailVerificationEndpoint;

    private final SpringTemplateEngine templateEngine;

    private final EmailNotificationSender emailNotificationSender;

    public void sendEmailConformation(String emailTo, String token) {
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
}
