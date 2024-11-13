/**
 * Entity representing authentication request data.
 * 
 * <p>Author: Akshay Dhere &lt;akshaydhere14@gmail.com&gt;</p>
 */

package com.spring.project.DataValidation.CrudApplication.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AuthRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generate ID
    private Long id;
    private String username; // Ensure this field exists
    private String password; // Ensure this field exists
    private String token; // If this is needed as well

    // Getters and Setters
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
}
