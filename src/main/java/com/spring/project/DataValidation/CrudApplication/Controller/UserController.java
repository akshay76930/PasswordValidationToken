package com.spring.project.DataValidation.CrudApplication.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.project.DataValidation.CrudApplication.Service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired	
    private UserService userService;

    @Operation(summary = "Request password reset", description = "Sends a password reset email to the specified email address")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password reset email sent"),
        @ApiResponse(responseCode = "400", description = "Invalid email format or user not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        try {
            userService.sendPasswordResetEmail(email);
            return ResponseEntity.ok("Password reset email sent.");
        } catch (IllegalArgumentException e) {
            // Handle invalid email or user not found
            return ResponseEntity.badRequest().body("Invalid email address.");
        } catch (Exception e) {
            // Handle general exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while sending the email.");
        }
    }

    @Operation(summary = "Reset password", description = "Resets the password using the provided token and new password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password has been reset"),
        @ApiResponse(responseCode = "400", description = "Invalid token or password format"),
        @ApiResponse(responseCode = "404", description = "Token not found or expired"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password has been reset.");
        } catch (IllegalArgumentException e) {
            // Handle invalid token or password format
            return ResponseEntity.badRequest().body("Invalid token or password format.");
        } catch (Exception e) {
            // Handle token not found or expired
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found or expired.");
        }
    }
}
