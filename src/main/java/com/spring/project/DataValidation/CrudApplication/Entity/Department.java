package com.spring.project.DataValidation.CrudApplication.Entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "department")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Employee> employees; // Set of employees in this department

    // Default constructor
    public Department() {}

    // Constructor with parameters
    public Department(String name) {
        this.name = name;
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

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    // Additional Methods

    /**
     * Adds an employee to the department.
     * 
     * @param employee the employee to add
     */
    public void addEmployee(Employee employee) {
        if (employees != null) {
            employees.add(employee);
            employee.setDepartment(this); // Set the reverse relationship
        }
    }

    /**
     * Removes an employee from the department.
     * 
     * @param employee the employee to remove
     */
    public void removeEmployee(Employee employee) {
        if (employees != null) {
            employees.remove(employee);
            employee.setDepartment(null); // Clear the reverse relationship
        }
    }

    /**
     * Checks if the department has any employees.
     * 
     * @return true if there are employees, false otherwise
     */
    public boolean hasEmployees() {
        return employees != null && !employees.isEmpty();
    }

    @Override
    public String toString() {
        return "Department [id=" + id + ", name=" + name + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
