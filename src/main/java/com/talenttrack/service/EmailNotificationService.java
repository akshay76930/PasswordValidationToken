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
public class EmailNotificationService {

    // Logger instance to log relevant information and errors
    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationService.class);

    // Injecting the JavaMailSender to send emails
    @Autowired
    private JavaMailSender mailSender;

    // Injecting the sender email address from application.properties
    @Value("${email.sender}")
    private String emailSender;

    /**
     * This method sends an email asynchronously using the specified subject.
     * It uses a simple email content template, but can be expanded to integrate with a more complex template engine.
     * 
     * @param to The recipient's email address
     * @param subject The subject of the email
     */
    @Async // Marks this method to be executed asynchronously, improving performance by not blocking the main thread
    public void sendEmailWithTemplate(String to, String subject) {
        // Example email content, this could be replaced with a dynamic template or content
        String emailContent = "This is a test email notification."; 
        
        // Attempt to send the email and log success/failure
        boolean isSent = sendEmail(to, subject, emailContent);

        if (isSent) {
            logger.info("Email sent successfully to {}", to); // Log if email is sent successfully
        } else {
            logger.warn("Failed to send email to {}", to); // Log a warning if sending fails
        }
    }

    /**
     * Method to construct and send an email.
     * 
     * @param to The recipient's email address
     * @param subject The subject of the email
     * @param content The body of the email
     * @return true if the email was sent successfully, false otherwise
     */
    private boolean sendEmail(String to, String subject, String content) {
        // Create a MimeMessage instance to represent the email
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        
        try {
            // Helper class to set various properties of the email like to, subject, content, etc.
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to); // Set recipient's email
            helper.setSubject(subject); // Set the email subject
            helper.setText(content, true); // Set the body content and specify HTML support
            helper.setFrom(emailSender); // Set the sender email address (from application.properties)
            
            // Send the email using the JavaMailSender
            mailSender.send(mimeMessage);
            return true; // Return true to indicate success
        } catch (MessagingException e) {
            // Log error if email sending fails
            logger.error("Failed to send email to {}: {}", to, e.getMessage());
            return false; // Return false to indicate failure
        }
    }
}
