package com.talenttrack.controller;

import com.talenttrack.exception.PasswordResetException;
import com.talenttrack.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserAuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthenticationController.class);

    private final UserService userService;

    @Autowired
    public UserAuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        if (!StringUtils.hasText(email)) {
            logger.warn("Email address is missing.");
            return ResponseEntity.badRequest().body("Email address is required.");
        }

        try {
            userService.sendPasswordResetEmail(email);
            logger.info("Password reset email sent to {}", email);
            return ResponseEntity.ok("Password reset email sent.");
        } catch (PasswordResetException e) {
            logger.error("Error sending password reset email to {}: {}", email, e.getMessage());
            return ResponseEntity.status(500).body("Failed to send password reset email: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error occurred while sending password reset email to {}: {}", email, e.getMessage());
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        if (!StringUtils.hasText(token) || !StringUtils.hasText(newPassword)) {
            logger.warn("Token or new password is missing.");
            return ResponseEntity.badRequest().body("Token and new password are required.");
        }

        // Example of password validation (you can enhance this)
        if (newPassword.length() < 8) {
            logger.warn("New password does not meet the minimum length requirement.");
            return ResponseEntity.badRequest().body("New password must be at least 8 characters long.");
        }

        try {
            boolean isResetSuccessful = userService.resetPassword(token, newPassword);
            if (isResetSuccessful) {
                logger.info("Password successfully reset for token {}", token);
                return ResponseEntity.ok("Password has been reset.");
            } else {
                logger.warn("Failed to reset password for token {}", token);
                return ResponseEntity.badRequest().body("Invalid token or unable to reset password.");
            }
        } catch (PasswordResetException e) {
            logger.error("Error resetting password for token {}: {}", token, e.getMessage());
            return ResponseEntity.status(500).body("Failed to reset password: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error occurred while resetting password for token {}: {}", token, e.getMessage());
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }
}
