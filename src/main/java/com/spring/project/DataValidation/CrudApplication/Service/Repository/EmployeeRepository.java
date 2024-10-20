package com.spring.project.DataValidation.CrudApplication.Service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.project.DataValidation.CrudApplication.Entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Custom query method to find an employee by email
    @Query("SELECT e FROM Employee e WHERE e.email = :email")
    Optional<Employee> findByEmail(@Param("email") String email);

    // Custom query method to find employees by gender
    List<Employee> findByGender(String gender);

    // Custom query method to find employees with a specific contact number
    @Query("SELECT e FROM Employee e WHERE e.contact = :contact")
    Optional<Employee> findByContact(@Param("contact") String contact);

    // Custom query method to find all employees with a specific name
    List<Employee> findByNameContaining(String name);

    // Custom query method to count employees by gender
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.gender = :gender")
    long countByGender(@Param("gender") String gender);
}
