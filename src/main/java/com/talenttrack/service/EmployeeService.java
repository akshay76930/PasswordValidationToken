package com.talenttrack.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.talenttrack.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee insertEmployee(Employee employee);

    Optional<Employee> updateEmployee(Long id, Employee employee);

    Optional<Employee> deleteEmployee(Long employeeId);

    List<Employee> searchEmployees(String name, String gender);

    List<Employee> getAllEmployees();

    Page<Employee> getEmployeesWithPagination(PageRequest pageRequest);

    Page<Employee> getEmployeesWithPagination(Pageable pageable);

    Optional<Employee> getEmployeeById(Long id);
}
