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

}
