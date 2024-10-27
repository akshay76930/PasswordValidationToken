package com.spring.project.DataValidation.CrudApplication.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

	private static final String SECRET_KEY = "1fDZFm6JPf9znRSbdNjLwMoeWyaWpE9Tz2xgYO9i2ow=";
	private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

	private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

	public String generateToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public String extractUsername(String token) {
		return getClaims(token).getSubject();
	}

	public boolean isTokenExpired(String token) {
		return getClaims(token).getExpiration().before(new Date());
	}

	public boolean validateToken(String token, String username) {
		return (extractUsername(token).equals(username) && !isTokenExpired(token));
	}

	private Claims getClaims(String token) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		} catch (Exception e) {
			System.out.println("Error parsing JWT: " + e.getMessage());
			return null;
		}
	}
}
