package com.spring.project.DataValidation.CrudApplication.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.spring.project.DataValidation.CrudApplication.Entity.Employee;

public interface EmployeeService {

    List<Employee> findAll();

    Employee insertEmployee(Employee employee);

    Employee updateEmployee(Employee employee);

    boolean deleteEmployee(Long employeeId);

    Optional<Employee> findById(Long id);

    List<Employee> searchEmployees(String name, String gender); // Add this method
    
    Page<Employee> findAllWithPagination(PageRequest pageRequest);

    // Default method to check if an employee exists by ID
    default boolean employeeExists(Long id) {
        return findById(id).isPresent();
    }

    // Static method for common utility, e.g., validating employee data
    static boolean isValidEmployee(Employee employee) {
        return employee != null && employee.getName() != null && !employee.getName().isEmpty();
    }
}
