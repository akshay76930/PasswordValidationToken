package com.spring.project.DataValidation.CrudApplication.Dao;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
import com.spring.project.DataValidation.CrudApplication.Exception.ResourceNotFoundException; // Make sure this import is present

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeDaoImpl {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    private final EmployeeService employeeService;
    private final EmailNotificationService emailNotificationService;

    public EmployeeDaoImpl(EmployeeService employeeService, EmailNotificationService emailNotificationService) {
        this.employeeService = employeeService;
        this.emailNotificationService = emailNotificationService;
    }

    @Operation(summary = "Get all employees", description = "Fetches all employees from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the list of employees")
    })
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        logger.info("Fetching all employees");
        List<Employee> employees = employeeService.findAll();
        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employees);
    }

    @Operation(summary = "Get an employee by ID", description = "Fetches an employee by their ID from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the employee"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        logger.info("Fetching employee with ID: {}", id);
        Employee employee = employeeService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        return ResponseEntity.ok(employee);
    }

    @Operation(summary = "Insert a new employee", description = "Adds a new employee to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee successfully created")
    })
    @PostMapping
    public ResponseEntity<Employee> insertEmployee(@Valid @RequestBody Employee employee) {
        logger.info("Inserting a new employee: {}", employee.getName());
        Employee insertedEmployee = employeeService.insertEmployee(employee);
        return ResponseEntity.status(201).body(insertedEmployee);
    }

    @Operation(summary = "Update an existing employee", description = "Updates the details of an existing employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee successfully updated"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employee) {
        logger.info("Updating employee with ID: {}", id);
        employee.setId(id);
        employeeService.updateEmployee(employee)
                .orElseThrow(() -> new ResourceNotFoundException("Failed to update employee with ID: " + id));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete an employee", description = "Removes an employee from the database by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        logger.info("Deleting employee with ID: {}", id);
        boolean isDeleted = employeeService.deleteEmployee(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Search employees", description = "Search employees by name and/or gender")
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(@RequestParam(required = false) String name,
                                                          @RequestParam(required = false) String gender) {
        logger.info("Searching employees by name: {} and gender: {}", name, gender);
        List<Employee> employees = employeeService.searchEmployees(name, gender);
        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employees);
    }

    @Operation(summary = "Get employees with pagination", description = "Fetches a paginated list of employees")
    @GetMapping("/page")
    public ResponseEntity<Page<Employee>> getEmployeesWithPagination(@RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(defaultValue = "id") String sortBy) {
        logger.info("Fetching employees page: {} size: {} sorted by: {}", page, size, sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Employee> employees = employeeService.findAllWithPagination(pageRequest);
        return ResponseEntity.ok(employees);
    }

    @Operation(summary = "Send an email", description = "Sends an email to the specified recipient.")
    @PostMapping("/send")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid email data"),
            @ApiResponse(responseCode = "500", description = "Internal server error while sending email")
    })
    public ResponseEntity<String> sendEmail(@RequestParam @NotNull String to, @RequestParam String subject) {
        logger.info("Sending email to: {}", to);
        emailNotificationService.sendEmailWithTemplate(to, subject);
        return ResponseEntity.ok("Email sent successfully to " + to);
    }
}
