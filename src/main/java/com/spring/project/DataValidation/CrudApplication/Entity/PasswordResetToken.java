package com.spring.project.DataValidation.CrudApplication.Entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "password_reset_tokens") // Make sure this matches your database schema
public class PasswordResetToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // Token should be unique
    private String token;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @ManyToOne(fetch = FetchType.LAZY) // Assuming you want to link to the User
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public PasswordResetToken() {}

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
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
