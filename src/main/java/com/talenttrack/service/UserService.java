package com.talenttrack.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.talenttrack.entity.PasswordResetToken;
import com.talenttrack.entity.User;
import com.talenttrack.repository.PasswordResetTokenRepository;
import com.talenttrack.repository.UserRepository;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailService;

    public UserService(UserRepository userRepository, PasswordResetTokenRepository tokenRepository,
                       PasswordEncoder passwordEncoder, EmailSenderService emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public boolean resetPassword(String token, String newPassword) {
        logger.info("Attempting to reset password with token: {}", token);

        Optional<PasswordResetToken> optionalToken = tokenRepository.findByToken(token);
        if (!optionalToken.isPresent()) {
            logger.warn("Invalid password reset token: {}", token);
            return false;
        }

        PasswordResetToken resetToken = optionalToken.get();

        if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            logger.warn("Expired password reset token: {}", token);
            tokenRepository.delete(resetToken);
            return false;
        }

        User user = resetToken.getUser();
        if (user == null) {
            logger.error("No user found for the provided token: {}", token);
            return false;
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        logger.info("Password successfully reset for user: {}", user.getUsername());

        tokenRepository.delete(resetToken);
        logger.info("Password reset token has been deleted after successful password reset");

        return true;
    }

    public void sendPasswordResetEmail(String email) {
        logger.info("Sending password reset email to: {}", email);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            logger.warn("No user found with email: {}", email);
            return;
        }

        User user = userOptional.get();

        String token = UUID.randomUUID().toString();
        LocalDateTime expirationDate = LocalDateTime.now().plusHours(1);

        PasswordResetToken passwordResetToken = new PasswordResetToken(token, expirationDate, user);
        tokenRepository.save(passwordResetToken);

        String resetLink = "http://your-frontend-url/reset-password?token=" + token;

        String subject = "Password Reset Request";
        String body = "To reset your password, click the link below:\n" + resetLink;

        emailService.sendEmailWithTemplate(email, subject, body);
        logger.info("Password reset email sent to: {}", email);
    }
}
