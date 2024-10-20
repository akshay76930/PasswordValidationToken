package com.spring.project.DataValidation.CrudApplication.Dao;

import java.util.List;
import java.util.Optional;

import com.spring.project.DataValidation.CrudApplication.Entity.Employee;

public interface EmployeeDao {

    List<Employee> findAll();

    boolean insertEmployee(Employee employee);

    boolean updateEmployee(Employee employee);

    boolean deleteEmployee(Long employeeId);

    Optional<Employee> findById(Long id);

    // Default method to check if an employee exists by ID, leveraging Java 8 Optional.
    default boolean employeeExists(Long id) {
        return findById(id).isPresent();
    }

    // Static method to provide common utility for DAOs if necessary.
    static String normalizeName(String name) {
        return name == null ? "" : name.trim().toUpperCase();
    }
}
