package com.spring.project.DataValidation.CrudApplication.Entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity  // This tells Hibernate to treat this class as a JPA entity
@Table(name = "employee")  // Optional: Specifies the table name
public class Employee implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id  // This is the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment strategy
    private Long id;

    private String name;
    private String contact;
    private String gender;
    private String email;
    private String password;
    private String username;  // Add this field

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// Default constructor
    public Employee() {}

    // Parameterized constructor
    public Employee(Long id, String name, String contact, String gender, String email, String password) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.gender = gender;
        this.email = email;
        this.password = password;
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

    // Override toString method
    @Override
    public String toString() {
        return "Employee [id=" + id + ", name=" + name + ", contact=" + contact + ", gender=" + gender + ", email=" + email + ", password=" + password + "]";
    }

    // Override equals and hashCode
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
