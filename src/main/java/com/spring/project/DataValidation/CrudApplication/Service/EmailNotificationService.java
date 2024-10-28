package com.spring.project.DataValidation.CrudApplication.Service;

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
public class EmailNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${email.sender}") // Inject the email sender from properties
    private String emailSender;

    @Async
    public void sendEmailWithTemplate(String to, String subject) {
        // Prepare the email content model (placeholders for the template)
        String emailContent = "This is a test email notification."; // You can expand this to use a proper template
        boolean isSent = sendEmail(to, subject, emailContent);

        if (isSent) {
            logger.info("Email sent successfully to {}", to);
        } else {
            logger.warn("Failed to send email to {}", to);
        }
    }

    // Method to send email with the filled template content
    private boolean sendEmail(String to, String subject, String content) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setFrom(emailSender); // Use the injected sender email
            mailSender.send(mimeMessage);
            return true; // Indicate success
        } catch (MessagingException e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage());
            return false; // Indicate failure
        }
    }
}
