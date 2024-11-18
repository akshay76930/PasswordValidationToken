package com.talenttrack.security;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

	// The secret key for signing and verifying JWT, injected from application
	// properties
	@Value("${jwt.secret.key}") // Make sure to set this in your application.properties
	private String secretKey;

	// The expiration time for access tokens (15 minutes)
	private static final long EXPIRATION_TIME = 1000 * 60 * 15;

	// SecretKey object for HMAC-based signing
	private SecretKey key;

	// PostConstruct method to initialize the secret key after bean creation
	@PostConstruct // Called once the bean is fully initialized and dependencies are injected
	public void init() {
		// Validate if the secret key is present, otherwise throw an exception
		if (secretKey == null || secretKey.isEmpty()) {
			logger.error("Secret key is not set or is empty");
			throw new IllegalArgumentException("Secret key must be provided");
		}
		// Convert the secret key to a SecretKey object using HMAC SHA-256 algorithm
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	/**
	 * Generates a JWT token with the given username.
	 * 
	 * @param username The username to be set as the subject in the token.
	 * @return A signed JWT token as a String.
	 */
	public String generateToken(String username) {
		// Create the JWT with a subject (username), issue date, and expiration time
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS256, key) // Use the HMAC SHA-256 algorithm with the secret key
				.compact();
	}

	/**
	 * Extracts the username (subject) from a JWT token.
	 * 
	 * @param token The JWT token.
	 * @return The username extracted from the token.
	 */
	public String extractUsername(String token) {
		return getClaims(token).getSubject();
	}

	/**
	 * Checks whether the given JWT token has expired.
	 * 
	 * @param token The JWT token.
	 * @return true if the token is expired, false otherwise.
	 */
	public boolean isTokenExpired(String token) {
		Date expiration = getClaims(token).getExpiration();
		return expiration != null && expiration.before(new Date());
	}

	/**
	 * Validates the token by comparing the extracted username and checking its
	 * expiration.
	 * 
	 * @param token    The JWT token.
	 * @param username The username to compare with the one in the token.
	 * @return true if the token is valid (username matches and token is not
	 *         expired).
	 */
	public boolean validateToken(String token, String username) {
		return extractUsername(token).equals(username) && !isTokenExpired(token);
	}

	/**
	 * Retrieves the claims from the JWT token. If the token is invalid or expired,
	 * it will log the error.
	 * 
	 * @param token The JWT token.
	 * @return Claims object containing the data in the token, or null if an error
	 *         occurs.
	 */
	private Claims getClaims(String token) {
		try {
			// Parse the token and get the claims (body of the JWT)
			return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			// Log if the token has expired
			logger.warn("Token expired: {}", e.getMessage());
		} catch (io.jsonwebtoken.MalformedJwtException e) {
			// Log if the token is malformed
			logger.error("Malformed token: {}", e.getMessage());
		} catch (io.jsonwebtoken.SignatureException e) {
			// Log if the token signature is invalid
			logger.error("Invalid signature: {}", e.getMessage());
		} catch (Exception e) {
			// Log any other errors that occur during parsing
			logger.error("Error parsing JWT: {}", e.getMessage());
		}
		return null; // Return null in case of error
	}
}
