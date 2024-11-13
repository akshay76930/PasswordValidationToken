/**
 * Entity class representing a User.
 * 
 * <p>Author: Akshay Dhere &lt;akshaydhere14@gmail.com&gt;</p>
 */

package com.spring.project.DataValidation.CrudApplication.Entity;

import javax.persistence.*;

/**
 * Entity class representing a User.
 */
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;  // Ensure this field exists
    
    @Column(name = "token")
    private String token;

    // Getters and Setters for the token field
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Constructors, getters, and setters
    public User() {}

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
