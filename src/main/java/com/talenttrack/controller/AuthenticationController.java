package com.talenttrack.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.talenttrack.entity.AuthRequest;
import com.talenttrack.entity.ErrorResponse;
import com.talenttrack.security.JwtUtil;
import com.talenttrack.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authRequestService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Autowired
    public AuthenticationController(AuthenticationService authRequestService,

            JwtUtil jwtUtil, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService) {
        this.authRequestService = authRequestService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> getToken(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            String token = jwtUtil.generateToken(userDetails.getUsername());

            logger.info("Token generated for user: {}", authRequest.getUsername());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            logger.warn("Login failed for user: {}", authRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody AuthRequest authRequest) {
        try {
            // Directly proceed with registration, no role check needed
            authRequest.setPassword(passwordEncoder.encode(authRequest.getPassword()));
            AuthRequest savedAuthRequest = authRequestService.saveAuthRequest(authRequest);
            logger.info("User registered successfully: {}", authRequest.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthRequest);
        } catch (Exception e) {
            logger.error("Registration failed for user: {}", authRequest.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Error during registration"));
        }
    }
}
