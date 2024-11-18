package com.talenttrack.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.talenttrack.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestParam String email) {
		try {
			userService.sendPasswordResetEmail(email);
			logger.info("Password reset email sent to {}", email);
			return ResponseEntity.ok("Password reset email sent.");
		} catch (Exception e) {
			logger.error("Error sending password reset email to {}: {}", email, e.getMessage());
			return ResponseEntity.status(500).body("Failed to send password reset email.");
		}
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
		if (token == null || token.isEmpty() || newPassword == null || newPassword.isEmpty()) {
			logger.warn("Token or new password is missing.");
			return ResponseEntity.badRequest().body("Token and new password are required.");
		}

		boolean isResetSuccessful = userService.resetPassword(token, newPassword);
		if (isResetSuccessful) {
			logger.info("Password successfully reset for token {}", token);
			return ResponseEntity.ok("Password has been reset.");
		} else {
			logger.warn("Failed to reset password for token {}", token);
			return ResponseEntity.badRequest().body("Invalid token or unable to reset password.");
		}
	}
}
