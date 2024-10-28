package com.spring.project.DataValidation.CrudApplication.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.spring.project.DataValidation.CrudApplication.Entity.Employee;

/**
 * DAO interface for Employee entity operations.
 */
@Repository
public interface EmployeeDao {

    /**
     * Retrieves all employees from the database.
     *
     * @return a list of all employees.
     */
    List<Employee> findAll();

    /**
     * Inserts a new employee into the database.
     *
     * @param employee the employee entity to be inserted.
     * @return the inserted employee entity.
     */
    Employee insertEmployee(Employee employee);

    /**
     * Updates an existing employee in the database.
     *
     * @param employee the employee entity with updated information.
     * @return true if the update was successful, false otherwise.
     */
    boolean updateEmployee(Employee employee);

    /**
     * Deletes an employee from the database by ID.
     *
     * @param employeeId the ID of the employee to be deleted.
     * @return true if the deletion was successful, false otherwise.
     */
    boolean deleteEmployee(Long employeeId);

    /**
     * Finds an employee by ID.
     *
     * @param id the ID of the employee to be found.
     * @return an Optional containing the found employee or empty if not found.
     */
    Optional<Employee> findById(Long id);

    /**
     * Checks if an employee exists in the database by ID.
     *
     * @param id the ID of the employee to check.
     * @return true if the employee exists, false otherwise.
     */
    default boolean employeeExists(Long id) {
        return findById(id).isPresent();
    }

    /**
     * Normalizes the name by trimming whitespace and converting to uppercase.
     *
     * @param name the name to normalize.
     * @return the normalized name.
     */
    static String normalizeName(String name) {
        return name == null ? "" : name.trim().toUpperCase();
    }

    // Optional: Add methods for batch operations if necessary.
    // List<Employee> insertEmployees(List<Employee> employees);
}
