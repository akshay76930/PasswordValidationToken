package com.talenttrack.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private static final Logger logger = LoggerFactory.getLogger(EmailSenderService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${email.sender}")
    private String emailSender;


    @Async
    public void sendEmailWithTemplate(String to, String subject, String content) {
        boolean isSent = sendEmail(to, subject, content);

        if (isSent) {
            logger.info("Email sent successfully to {}", to);
        } else {
            logger.warn("Failed to send email to {}", to);
        }
    }

   
    private boolean sendEmail(String to, String subject, String content) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);  // Use HTML content
            helper.setFrom(emailSender);

            mailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            // Log the exception stack trace for better debugging
            logger.error("Failed to send email to {}: {}", to, e.getMessage(), e);
            return false;
        }
    }
}
