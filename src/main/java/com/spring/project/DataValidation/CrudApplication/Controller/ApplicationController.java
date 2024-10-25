package com.spring.project.DataValidation.CrudApplication.Controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.project.DataValidation.CrudApplication.Entity.Employee;
import com.spring.project.DataValidation.CrudApplication.Service.EmailNotificationService;
import com.spring.project.DataValidation.CrudApplication.Service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/employees")
public class ApplicationController {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

	private final EmployeeService employeeService;
	private final EmailNotificationService emailNotificationService;

	public ApplicationController(EmployeeService employeeService, EmailNotificationService emailNotificationService) {
		this.employeeService = employeeService;
		this.emailNotificationService = emailNotificationService;
	}

	@Operation(summary = "Get all employees", description = "Fetches all employees from the database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully fetched the list of employees") })
	@GetMapping
	public ResponseEntity<List<Employee>> getAllEmployees() {
		logger.info("Request received: Fetching all employees");
		List<Employee> employees = Optional.ofNullable(employeeService.findAll()).orElseThrow(() -> {
			logger.warn("No employees found in the database");
			return new RuntimeException("No employees found");
		});
		logger.info("Fetched {} employees", employees.size());
		return ResponseEntity.ok(employees);
	}

	@Operation(summary = "Get an employee by ID", description = "Fetches an employee by their ID from the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully fetched the employee"),
			@ApiResponse(responseCode = "404", description = "Employee not found") })
	@GetMapping("/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
		logger.info("Request received: Fetching employee with ID: {}", id);
		Employee employee = employeeService.findById(id).orElseThrow(() -> {
			logger.error("Employee with ID {} not found.", id);
			return new RuntimeException("Employee not found");
		});
		logger.info("Found employee: {}", employee);
		return ResponseEntity.ok(employee);
	}

	@Operation(summary = "Insert a new employee", description = "Adds a new employee to the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Employee successfully created") })
	@PostMapping
	public ResponseEntity<Employee> insertEmployee(@Valid @RequestBody Employee employee) {
		logger.info("Request received: Inserting a new employee with name: {}", employee.getName());
		Employee insertedEmployee = employeeService.insertEmployee(employee);
		logger.info("Employee inserted successfully with ID: {}", insertedEmployee.getId());
		return ResponseEntity.status(201).body(insertedEmployee);
	}

	@Operation(summary = "Update an existing employee", description = "Updates the details of an existing employee")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Employee successfully updated"),
			@ApiResponse(responseCode = "404", description = "Employee not found") })
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employee) {
		logger.info("Request received: Updating employee with ID: {}", id);
		employee.setId(id);
		Optional.ofNullable(employeeService.updateEmployee(employee)).orElseThrow(() -> {
			logger.error("Failed to update employee with ID: {}", id);
			return new RuntimeException("Failed to update employee");
		});
		logger.info("Employee with ID: {} updated successfully", id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Delete an employee", description = "Removes an employee from the database by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Employee successfully deleted"),
			@ApiResponse(responseCode = "404", description = "Employee not found") })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
		logger.info("Request received: Deleting employee with ID: {}", id);
		boolean isDeleted = employeeService.deleteEmployee(id);
		if (isDeleted) {
			logger.info("Employee with ID: {} deleted successfully", id);
			return ResponseEntity.noContent().build();
		} else {
			logger.warn("Employee with ID: {} not found for deletion", id);
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Search employees", description = "Search employees by name and/or gender")
	@GetMapping("/search")
	public ResponseEntity<List<Employee>> searchEmployees(@RequestParam(required = false) String name,
			@RequestParam(required = false) String gender) {
		logger.info("Request received: Searching employees by name: {} and gender: {}", name, gender);
		List<Employee> employees = employeeService.searchEmployees(name, gender);
		if (employees.isEmpty()) {
			logger.warn("No employees found matching criteria - name: {} gender: {}", name, gender);
			return ResponseEntity.noContent().build();
		}
		logger.info("Found {} employees matching search criteria", employees.size());
		return ResponseEntity.ok(employees);
	}

	@Operation(summary = "Get employees with pagination", description = "Fetches a paginated list of employees")
	@GetMapping("/page")
	public ResponseEntity<Page<Employee>> getEmployeesWithPagination(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
		logger.info("Request received: Fetching employees page: {} size: {} sorted by: {}", page, size, sortBy);
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
		Page<Employee> employees = employeeService.findAllWithPagination(pageRequest);
		logger.info("Fetched page {} of employees with page size {}", page, size);
		return ResponseEntity.ok(employees);
	}

	@Operation(summary = "Send an email", description = "Sends an email to the specified recipient.")
	@PostMapping("/send")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Email sent successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid email data"),
			@ApiResponse(responseCode = "500", description = "Internal server error while sending email") })
	public ResponseEntity<String> sendEmail(@RequestParam String to, @RequestParam String subject) {
		logger.info("Request received: Sending email to: {}", to);
		try {
			emailNotificationService.sendEmailWithTemplate(to, subject);
			logger.info("Email sent successfully to {}", to);
			return ResponseEntity.ok("Email sent successfully to " + to);
		} catch (Exception e) {
			logger.error("Failed to send email to {} with subject {}: {}", to, subject, e.getMessage());
			return ResponseEntity.status(500).body("Failed to send email");
		}
	}
}
