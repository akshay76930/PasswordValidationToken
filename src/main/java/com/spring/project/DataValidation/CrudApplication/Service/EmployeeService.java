package com.spring.project.DataValidation.CrudApplication.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.spring.project.DataValidation.CrudApplication.Entity.Employee;

public interface EmployeeService {

    List<Employee> findAll();

    Employee insertEmployee(Employee employee);

    Employee updateEmployee(Long id, Employee employee);

    boolean deleteEmployee(Long employeeId);

    Optional<Employee> findById(Long id);

    List<Employee> searchEmployees(String name, String gender);

    Page<Employee> findAllWithPagination(PageRequest pageRequest);

}
