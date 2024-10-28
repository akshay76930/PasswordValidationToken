package com.spring.project.DataValidation.CrudApplication.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class representing an authentication request.
 */
@Entity
@Table(name = "auth_request") // Custom table name
public class AuthRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use IDENTITY strategy for auto-increment
    private Long id;

    @Column(nullable = false, unique = true, length = 50) // Ensure username is unique and not null
    private String username;

    @Column(nullable = false) // Password should not be null
    private String password;

    // Default constructor
    public AuthRequest() {}

    // Parameterized constructor
    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password; // Consider hashing this password before setting it
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
        this.password = password; // Consider hashing this password before setting it
    }

    @Override
    public String toString() {
        return "AuthRequest{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthRequest)) return false;
        AuthRequest that = (AuthRequest) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31; // Default hash code for the entity
    }
}
