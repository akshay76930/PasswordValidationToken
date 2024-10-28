package com.spring.project.DataValidation.CrudApplication.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.project.DataValidation.CrudApplication.Entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Finds an employee by their email.
     *
     * @param email the email address to search for.
     * @return an Optional containing the found Employee or empty if not found.
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Finds all employees of a specific gender.
     *
     * @param gender the gender to filter employees by.
     * @return a list of Employees matching the specified gender.
     */
    List<Employee> findByGender(String gender);

    /**
     * Finds an employee by their contact number.
     *
     * @param contact the contact number to search for.
     * @return an Optional containing the found Employee or empty if not found.
     */
    Optional<Employee> findByContact(String contact);

    /**
     * Finds employees whose names contain the specified string (case-insensitive).
     *
     * @param name the string to search for in employee names.
     * @return a list of Employees whose names contain the specified string.
     */
    List<Employee> findByNameContainingIgnoreCase(String name);

    /**
     * Counts the number of employees of a specific gender.
     *
     * @param gender the gender to count employees by.
     * @return the count of Employees matching the specified gender.
     */
    long countByGender(String gender);

    /**
     * Finds employees by their name (exact match).
     *
     * @param name the exact name to search for.
     * @return a list of Employees with the specified name.
     */
    List<Employee> findByName(String name);

    /**
     * Finds employees by both name and gender.
     *
     * @param name   the name to filter employees by.
     * @param gender the gender to filter employees by.
     * @return a list of Employees matching the specified name and gender.
     */
    List<Employee> findByNameAndGender(String name, String gender);
    
    /**
     * Finds all employees ordered by their name.
     *
     * @return a list of all Employees ordered by name.
     */
    @Query("SELECT e FROM Employee e ORDER BY e.name")
    List<Employee> findAllOrderedByName();
    
    /**
     * Finds employees by name starting with a specific prefix (case-insensitive).
     *
     * @param prefix the prefix to search for in employee names.
     * @return a list of Employees whose names start with the specified prefix.
     */
    @Query("SELECT e FROM Employee e WHERE LOWER(e.name) LIKE LOWER(CONCAT(:prefix, '%'))")
    List<Employee> findByNameStartingWith(@Param("prefix") String prefix);
}
