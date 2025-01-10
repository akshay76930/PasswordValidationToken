package com.talenttrack.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity representing authentication request data.
 *
 * <p>
 * Author: Akshay Dhere &lt;akshaydhere14@gmail.com&gt;
 * </p>
 */
@Entity
public class AuthRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	private String password;
	private String token;
	private String role;

	// No-argument constructor required by Hibernate
	public AuthRequest() {
	}

	// Parameterized constructor for convenient usage
	public AuthRequest(Long id, String username, String password, String token) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.token = token;
	}

	// Builder-based constructor
	private AuthRequest(AuthRequestBuilder builder) {
		this.id = builder.id;
		this.username = builder.username;
		this.password = builder.password;
		this.token = builder.token;
	}

	// Getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	// Builder class
	public static class AuthRequestBuilder {
		private Long id;
		private String username;
		private String password;
		private String token;

		public AuthRequestBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public AuthRequestBuilder username(String username) {
			this.username = username;
			return this;
		}

		public AuthRequestBuilder password(String password) {
			this.password = password;
			return this;
		}

		public AuthRequestBuilder token(String token) {
			this.token = token;
			return this;
		}

		public AuthRequest build() {
			return new AuthRequest(this);
		}
	}
}
