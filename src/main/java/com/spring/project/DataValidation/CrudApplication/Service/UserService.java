package com.spring.project.DataValidation.CrudApplication.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.project.DataValidation.CrudApplication.Entity.PasswordResetToken;
import com.spring.project.DataValidation.CrudApplication.Entity.User;
import com.spring.project.DataValidation.CrudApplication.Repository.PasswordResetTokenRepository;
import com.spring.project.DataValidation.CrudApplication.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String email) {
        // Log the email input
        System.out.println("Email passed: " + email);
        
        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found for email: " + email));

        // Generate a reset token
        String token = UUID.randomUUID().toString();
        System.out.println("Generated token: " + token);
        
        // Create password reset token entity
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpirationDate(LocalDateTime.now().plusHours(1)); // Set expiration time (e.g., 1 hour)

        // Save the token in the database
        tokenRepository.save(passwordResetToken);
        System.out.println("Token saved successfully!");

        // Create the reset link
        String resetLink = "http://localhost:8080/reset-password?token=" + token;
        System.out.println("Reset link: " + resetLink);

        // Send email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n" + resetLink);
        
        // Send the email
        mailSender.send(message);
        System.out.println("Password reset email sent successfully!");
    }

    public void resetPassword(String token, String newPassword) {
        // Validate the token
        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        // Check if the token has expired
        if (passwordResetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        // Get the user associated with the token
        User user = passwordResetToken.getUser();

        // Update the user's password (you may want to hash the password before saving)
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);

        // Optionally delete the token after use
        tokenRepository.delete(passwordResetToken);
    }
}
