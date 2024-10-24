package com.spring.project.DataValidation.CrudApplication.Controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.project.DataValidation.CrudApplication.Entity.Employee;
import com.spring.project.DataValidation.CrudApplication.Service.EmailNotificationService;
import com.spring.project.DataValidation.CrudApplication.Service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/employees")
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
		logger.info("Fetching all employees");

		List<Employee> employees = Optional.ofNullable(employeeService.findAll())
				.orElseThrow(() -> new RuntimeException("No employees found"));

		return ResponseEntity.ok(employees);
	}

	@Operation(summary = "Get an employee by ID", description = "Fetches an employee by their ID from the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully fetched the employee"),
			@ApiResponse(responseCode = "404", description = "Employee not found") })
	@GetMapping("/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
		logger.info("Fetching employee with ID: {}", id);

		Employee employee = employeeService.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));

		return ResponseEntity.ok(employee);
	}

	@Operation(summary = "Insert a new employee", description = "Adds a new employee to the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Employee successfully created") })
	@PostMapping
	public ResponseEntity<Employee> insertEmployee(@Valid @RequestBody Employee employee) {
		logger.info("Inserting a new employee: {}", employee.getName());

		Employee insertedEmployee = employeeService.insertEmployee(employee);
		return ResponseEntity.status(201).body(insertedEmployee);
	}

	@Operation(summary = "Update an existing employee", description = "Updates the details of an existing employee")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Employee successfully updated"),
			@ApiResponse(responseCode = "404", description = "Employee not found") })
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employee) {
		logger.info("Updating employee with ID: {}", id);

		employee.setId(id);

		Optional.ofNullable(employeeService.updateEmployee(employee))
				.orElseThrow(() -> new RuntimeException("Failed to update employee"));

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/send")
	@Operation(summary = "Send an email", description = "Sends an email to the specified recipient.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Email sent successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid email data"),
			@ApiResponse(responseCode = "500", description = "Internal server error while sending email") })
	public ResponseEntity<String> sendEmail(@RequestParam String to, @RequestParam String subject) {
		emailNotificationService.sendEmailWithTemplate(to, subject);
		return ResponseEntity.ok("Email sent successfully to " + to);
	}

	@Operation(summary = "Delete an employee", description = "Removes an employee from the database by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Employee successfully deleted"),
			@ApiResponse(responseCode = "404", description = "Employee not found") })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
		logger.info("Deleting employee with ID: {}", id);

		boolean isDeleted = employeeService.deleteEmployee(id);

		if (isDeleted) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
