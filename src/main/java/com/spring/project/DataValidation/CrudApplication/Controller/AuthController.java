package com.spring.project.DataValidation.CrudApplication.Controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.project.DataValidation.CrudApplication.Entity.AuthRequest;
import com.spring.project.DataValidation.CrudApplication.Entity.User;
import com.spring.project.DataValidation.CrudApplication.Repository.AuthRequestRepository;
import com.spring.project.DataValidation.CrudApplication.Repository.UserRepository;
import com.spring.project.DataValidation.CrudApplication.Security.JwtUtil;
import com.spring.project.DataValidation.CrudApplication.Service.AuthRequestService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthRequestService authRequestService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthRequestRepository authRequestRepository;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Autowired
    public AuthController(AuthRequestService authRequestService, UserRepository userRepository, JwtUtil jwtUtil,
    		BCryptPasswordEncoder passwordEncoder, AuthRequestRepository authRequestRepository) {
        this.authRequestService = authRequestService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
		this.authRequestRepository = authRequestRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<String> getToken(@RequestBody AuthRequest authRequest) {
        // Retrieve the user based on the username
        Optional<AuthRequest> userOptional = authRequestRepository.findByUsername(authRequest.getUsername());

        return userOptional.filter(user -> passwordEncoder.matches(authRequest.getPassword(), user.getPassword()))
                .map(user -> {
                    // Generate the JWT token for the authenticated user
                    String token = jwtUtil.generateToken(user.getUsername());
                    logger.info("Token generated for user: {}", authRequest.getUsername());
                    return ResponseEntity.ok(token);
                })
                .orElseGet(() -> {
                    // Log warning and return Unauthorized status for invalid credentials
                    logger.warn("Login failed for user: {}", authRequest.getUsername());
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
                });
    }



    @PostMapping("/register")
    public ResponseEntity<AuthRequest> register(@RequestBody AuthRequest authRequest) {
        authRequest.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        AuthRequest savedAuthRequest = authRequestService.saveAuthRequest(authRequest);
        logger.info("User registered successfully: {}", authRequest.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthRequest);
    }

    @PostMapping("/test-token")
    public ResponseEntity<String> testToken() {
        String token = jwtUtil.generateToken("testUser");
        logger.info("Test token generated for 'testUser'");
        return ResponseEntity.ok(token);
    }
}
