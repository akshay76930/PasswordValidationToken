package com.spring.project.DataValidation.CrudApplication.Security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.annotation.PostConstruct;

@Component
public class JwtUtil {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

	// Load the secret key from environment variable or application.properties
	@Value("${jwt.secret.key}") // Set this in your application.properties
	private String secretKey;

	private static final long EXPIRATION_TIME = 1000 * 60 * 15; // 15 minutes for access tokens

	private SecretKey key;

	@PostConstruct // This method will be called after the bean is created and dependencies are
					// injected
	public void init() {
		if (secretKey == null || secretKey.isEmpty()) {
			logger.error("Secret key is not set or is empty");
			throw new IllegalArgumentException("Secret key must be provided");
		}
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	public String generateToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS256, key).compact();
	}

	public String extractUsername(String token) {
		return getClaims(token).getSubject();
	}

	public boolean isTokenExpired(String token) {
		Date expiration = getClaims(token).getExpiration();
		return expiration != null && expiration.before(new Date());
	}

	public boolean validateToken(String token, String username) {
		return extractUsername(token).equals(username) && !isTokenExpired(token);
	}

	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			logger.warn("Token expired: {}", e.getMessage());
		} catch (io.jsonwebtoken.MalformedJwtException e) {
			logger.error("Malformed token: {}", e.getMessage());
		} catch (io.jsonwebtoken.SignatureException e) {
			logger.error("Invalid signature: {}", e.getMessage());
		} catch (Exception e) {
			logger.error("Error parsing JWT: {}", e.getMessage());
		}
		return null;
	}

}
