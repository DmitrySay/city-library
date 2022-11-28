package com.example.service;


import com.example.dto.NotificationTemplate;
import com.example.exception.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Slf4j
@EnableAsync
@Service
@RequiredArgsConstructor
public class EmailNotificationSender {

    @Value("${spring.mail.username}")
    private String username;

    private final JavaMailSender javaMailSender;

    @Async
    public synchronized void sendEmailNotification(NotificationTemplate notificationTemplate) {
        log.info("Start sending email notification");
        try {
            String[] mailTo = notificationTemplate.getMailTo().toArray(new String[0]);
            String emailSubject = notificationTemplate.getEmailSubject();
            String emailBody = notificationTemplate.getEmailBody();

            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);
            messageHelper.setFrom(username);
            messageHelper.setTo(mailTo);
            messageHelper.setSubject(emailSubject);
            messageHelper.setText(emailBody, true);

            javaMailSender.send(mailMessage);
            log.info("Email sent to {}", String.join(",", mailTo));

        } catch (MessagingException ex) {
            log.error("Can't send email notification");
            throw new RestException("Can't send email notification");
        }
    }

}
