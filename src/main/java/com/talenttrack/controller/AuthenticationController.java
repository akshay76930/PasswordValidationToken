package com.talenttrack.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talenttrack.entity.AuthRequest;
import com.talenttrack.entity.ErrorResponse;
import com.talenttrack.entity.Role;
import com.talenttrack.repository.AuthRequestRepository;
import com.talenttrack.repository.UserRepository;
import com.talenttrack.security.JwtUtil;
import com.talenttrack.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authRequestService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthRequestRepository authRequestRepository;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Autowired
    public AuthenticationController(AuthenticationService authRequestService, UserRepository userRepository,
                                     JwtUtil jwtUtil, BCryptPasswordEncoder passwordEncoder, AuthRequestRepository authRequestRepository) {
        this.authRequestService = authRequestService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authRequestRepository = authRequestRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<String> getToken(@RequestBody AuthRequest authRequest) {
        Optional<AuthRequest> userOptional = authRequestRepository.findByUsername(authRequest.getUsername());

        return userOptional.filter(user -> passwordEncoder.matches(authRequest.getPassword(), user.getPassword()))
                .map(user -> {
                    if ("ADMIN".equals(user.getRole())) { 
                        logger.warn("Access denied for user with ADMIN role: {}", authRequest.getUsername());
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied for this role");
                    }
                    
                    String token = jwtUtil.generateToken(user.getUsername());
                    logger.info("Token generated for user: {}", authRequest.getUsername());
                    return ResponseEntity.ok(token);
                }).orElseGet(() -> {
                    logger.warn("Login failed for user: {}", authRequest.getUsername());
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
                });
    }


    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> register(@RequestBody AuthRequest authRequest) {
        try {
            
            logger.info("Received role: {}", authRequest.getRole());

            Role role = Role.valueOf(authRequest.getRole());
            if (role == Role.ADMIN) {
                logger.warn("Attempt to register with ADMIN role: {}", authRequest.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body(new ErrorResponse("Registration with ADMIN role is not allowed"));
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid role provided: {}", authRequest.getRole());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new ErrorResponse("Invalid role provided: " + authRequest.getRole()));
        }

        authRequest.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        AuthRequest savedAuthRequest = authRequestService.saveAuthRequest(authRequest);
        logger.info("User registered successfully with role {}: {}", authRequest.getRole(), authRequest.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthRequest);
    }
}
