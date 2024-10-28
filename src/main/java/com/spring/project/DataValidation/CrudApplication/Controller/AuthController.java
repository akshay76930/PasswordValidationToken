package com.spring.project.DataValidation.CrudApplication.Controller;

import com.spring.project.DataValidation.CrudApplication.Entity.AuthRequest;
import com.spring.project.DataValidation.CrudApplication.Entity.User;
import com.spring.project.DataValidation.CrudApplication.Repository.UserRepository;
import com.spring.project.DataValidation.CrudApplication.Security.JwtUtil;
import com.spring.project.DataValidation.CrudApplication.Service.AuthRequestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthRequestService authRequestService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Autowired
    public AuthController(AuthRequestService authRequestService, UserRepository userRepository,
                          JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.authRequestService = authRequestService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        Optional<User> userOptional = userRepository.findByUsername(authRequest.getUsername());

        return userOptional.filter(user -> passwordEncoder.matches(authRequest.getPassword(), user.getPassword()))
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getUsername());
                    logger.info("Token generated for user: {}", authRequest.getUsername());
                    return ResponseEntity.ok(token);
                })
                .orElseGet(() -> {
                    logger.warn("Login failed for user: {}", authRequest.getUsername());
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
                });
    }

    @PostMapping("/register")
    public ResponseEntity<AuthRequest> register(@RequestBody AuthRequest authRequest) {
        authRequest.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        AuthRequest savedAuthRequest = authRequestService.saveAuthRequest(authRequest);
        logger.info("User registered successfully: {}", authRequest.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthRequest); // Changed to 201 Created
    }

    @PostMapping("/test-token")
    public ResponseEntity<String> testToken() {
        String testUsername = "test user";
        String token = jwtUtil.generateToken(testUsername);
        return ResponseEntity.ok(token);
    }
}
