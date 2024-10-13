package com.spring.project.DataValidation.CrudApplication.Service.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.project.DataValidation.CrudApplication.Entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	
	Optional<Employee> findByUsername(String username); // Method to find user by username
}
