package com.spring.project.DataValidation.CrudApplication.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.spring.project.DataValidation.CrudApplication.Entity.Department;
import com.spring.project.DataValidation.CrudApplication.Entity.Employee;

public interface EmployeeService {

	/**
	 * Inserts a new employee into the system.
	 *
	 * @param employee The employee object to be added.
	 * @return The saved employee object after insertion.
	 */
	Employee insertEmployee(Employee employee);

	/**
	 * Updates the details of an existing employee.
	 *
	 * @param id       The ID of the employee to update.
	 * @param employee The employee object with updated details.
	 * @return true if the employee was updated successfully, false otherwise.
	 */
	boolean updateEmployee(Long id, Employee employee);

	/**
	 * Deletes an employee by their ID.
	 *
	 * @param employeeId The ID of the employee to delete.
	 * @return true if the employee was deleted successfully, false otherwise.
	 */
	boolean deleteEmployee(Long employeeId);

	/**
	 * Searches employees by name and gender.
	 *
	 * @param name   The name of the employee to search.
	 * @param gender The gender of the employee to search.
	 * @return A list of employees matching the search criteria.
	 */
	List<Employee> searchEmployees(String name, String gender);

	/**
	 * Retrieves all employees in the system.
	 *
	 * @return A list of all employees.
	 */
	List<Employee> getAllEmployees();

	/**
	 * Retrieves a paginated list of employees.
	 *
	 * @param pageRequest The pagination information.
	 * @return A paginated list of employees.
	 */
	Page<Employee> getEmployeesWithPagination(PageRequest pageRequest);

	/**
	 * Retrieves a paginated list of employees.
	 *
	 * @param pageable The pagination information.
	 * @return A paginated list of employees.
	 */
	Page<Employee> getEmployeesWithPagination(Pageable pageable);

	/**
	 * Retrieves an employee by their ID.
	 *
	 * @param id The ID of the employee to fetch.
	 * @return An Optional containing the employee if found, or an empty Optional if
	 *         not found.
	 */
	Optional<Employee> getEmployeeById(Long id);
}