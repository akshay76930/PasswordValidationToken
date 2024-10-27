package com.spring.project.DataValidation.CrudApplication.Controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.project.DataValidation.CrudApplication.Entity.AuthRequest;
import com.spring.project.DataValidation.CrudApplication.Entity.User;
import com.spring.project.DataValidation.CrudApplication.Repository.UserRepository;
import com.spring.project.DataValidation.CrudApplication.Security.JwtUtil;
import com.spring.project.DataValidation.CrudApplication.Service.AuthRequestService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthRequestService authRequestService;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder; // Add this line

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class); // Logger initialization

	@Value("${jwt.expiration}")
	private long jwtExpiration;

	@Autowired
	public AuthController(AuthRequestService authRequestService, UserRepository userRepository, JwtUtil jwtUtil,
			PasswordEncoder passwordEncoder) {
		this.authRequestService = authRequestService;
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
		this.passwordEncoder = passwordEncoder; // Inject PasswordEncoder
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
		logger.info("Login request received for user: {}", authRequest.getUsername());

		Optional<User> userOpt = userRepository.findByUsername(authRequest.getUsername());

		if (userOpt.isPresent() && passwordEncoder.matches(authRequest.getPassword(), userOpt.get().getPassword())) {
			String token = jwtUtil.generateToken(userOpt.get().getUsername());
			logger.info("Login successful for user: {}", authRequest.getUsername());
			return ResponseEntity.ok(token);
		} else {
			logger.warn("Login failed for user: {}", authRequest.getUsername());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		}
	}
	@PostMapping("/register")
	public ResponseEntity<AuthRequest> register(@RequestBody AuthRequest authRequest) {
	    authRequest.setPassword(passwordEncoder.encode(authRequest.getPassword()));
	    AuthRequest savedAuthRequest = authRequestService.saveAuthRequest(authRequest);
	    logger.info("User registered successfully: {}", authRequest.getUsername());
	    return ResponseEntity.ok(savedAuthRequest);
	}
	
	@PostMapping("/test-token")
	public ResponseEntity<String> testToken() {
	    String testUsername = "nisha";
	    String token = jwtUtil.generateToken(testUsername);
	    return ResponseEntity.ok(token);
	}

}
