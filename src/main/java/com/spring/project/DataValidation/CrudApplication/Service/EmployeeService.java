package com.spring.project.DataValidation.CrudApplication.Service;

import java.util.List;
import java.util.Optional;

import com.spring.project.DataValidation.CrudApplication.Entity.Employee;

public interface EmployeeService {

    // Method to fetch all employees
    List<Employee> findAll();

    // Method to insert a new employee
    Employee insertEmployee(Employee employee);

    // Method to update an existing employee
    Employee updateEmployee(Employee employee);

    // Method to delete an employee by ID
    boolean deleteEmployee(Long employeeId);

    // Method to find an employee by ID
    Optional<Employee> findById(Long id);
}
