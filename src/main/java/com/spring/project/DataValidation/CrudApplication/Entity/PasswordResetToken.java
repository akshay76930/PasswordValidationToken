/**
 * Entity class representing a password reset token.
 * 
 * <p>Author: Akshay Dhere &lt;akshaydhere14@gmail.com&gt;</p>
 */

package com.spring.project.DataValidation.CrudApplication.Entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entity class representing a password reset token.
 */
@Entity
@Table(name = "password_reset_tokens") // Ensure this matches your database schema
public class PasswordResetToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255) // Token should be unique
    private String token;

    @Column(nullable = false) // Expiration date cannot be null
    private LocalDateTime expirationDate;

    @ManyToOne(fetch = FetchType.LAZY) // Assuming you want to link to the User
    @JoinColumn(name = "user_id", nullable = false) // Foreign key for the User entity
    private User user;

    // Default constructor
    public PasswordResetToken() {}

    // Parameterized constructor
    public PasswordResetToken(String token, LocalDateTime expirationDate, User user) {
        this.token = token;
        this.expirationDate = expirationDate;
        this.user = user;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public User getUser() {
        return user; // Getter for User reference
    }

    public void setUser(User user) {
        this.user = user; // Setter for User reference
    }

    @Override
    public String toString() {
        return "PasswordResetToken [id=" + id + ", token=" + token + ", expirationDate=" + expirationDate + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordResetToken that = (PasswordResetToken) o;
        return Objects.equals(id, that.id) && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token);
    }
}
