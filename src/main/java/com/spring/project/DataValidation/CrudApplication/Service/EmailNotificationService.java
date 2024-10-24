package com.spring.project.DataValidation.CrudApplication.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationService {

	@Autowired
	private JavaMailSender mailSender;

	@Async
	public void sendEmailWithTemplate(String to, String subject) {
		// Prepare the email content model (placeholders for the template)
		String emailContent = "This is a test email notification.";
		sendEmail(to, subject, emailContent);
	}

	// Method to send email with the filled template content
	private void sendEmail(String to, String subject, String content) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);
			helper.setFrom("akshaydhere14@gmail.com");
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			// Log the error (you can add proper error handling here)
			e.printStackTrace();
		}
	}
}
