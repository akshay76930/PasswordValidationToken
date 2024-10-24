package com.spring.project.DataValidation.CrudApplication.Entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Name cannot be null
    private String name;

    private String contact;

    private String gender;

    @Column(nullable = false, unique = true) // Email must be unique and cannot be null
    private String email;

    @Column(nullable = false) // Password cannot be null
    private String password;

    @Column(nullable = false) // Expiration date cannot be null
    private LocalDateTime expirationDate;

    @ManyToOne(fetch = FetchType.LAZY) // Many Employees can relate to one User
    @JoinColumn(name = "user_id", nullable = false) // Foreign key for the User entity
    private User user; // Reference to User entity

    // Default constructor
    public Employee() {}

    // Constructor with parameters
    public Employee(Long id, String name, String contact, String gender, String email, String password, LocalDateTime expirationDate, User user) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.expirationDate = expirationDate;
        this.user = user; // Initialize User reference
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return "Employee [id=" + id + ", name=" + name + ", contact=" + contact + ", gender=" + gender + ", email=" + email + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(email, employee.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
