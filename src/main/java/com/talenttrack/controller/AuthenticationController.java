package com.talenttrack.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talenttrack.entity.AuthRequest;
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
					// Generate the JWT token for the authenticated user
					String token = jwtUtil.generateToken(user.getUsername());
					logger.info("Token generated for user: {}", authRequest.getUsername());
					return ResponseEntity.ok(token);
				}).orElseGet(() -> {
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

}
