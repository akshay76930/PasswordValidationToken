package com.talenttrack.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;


import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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

import com.talenttrack.entity.Employee;
import com.talenttrack.response.ApiResponse;
import com.talenttrack.service.EmailSenderService;
import com.talenttrack.service.EmployeeService;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;
    private final EmailSenderService emailNotificationService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, EmailSenderService emailNotificationService) {
        this.employeeService = employeeService;
        this.emailNotificationService = emailNotificationService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Employee>>> getAllEmployees() {
        logger.info("Fetching all employees");
        List<Employee> employees = employeeService.getAllEmployees();
        return employees.isEmpty() ? buildNoContentResponse() : ResponseEntity.ok(new ApiResponse<>(employees));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<Employee>> getEmployeeById(@PathVariable Long id) {
        logger.info("Fetching employee with ID: {}", id);
        return employeeService.getEmployeeById(id)
                .map(employee -> ResponseEntity.ok(new ApiResponse<>(employee)))
                .orElseGet(() -> buildNotFoundResponse("Employee not found"));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Employee>> insertEmployee(@Valid @RequestBody Employee employee) {
        logger.info("Inserting a new employee: {}", employee.getName());
        Employee savedEmployee = employeeService.insertEmployee(employee);
        return buildCreatedResponse(new ApiResponse<>(savedEmployee));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateEmployee(@PathVariable Long id,
            @Valid @RequestBody Employee employee) {
        logger.info("Updating employee with ID: {}", id);
        return employeeService.updateEmployee(id, employee) ? ResponseEntity.ok(new ApiResponse<>(null))
                : buildNotFoundResponse("Employee not found");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        logger.info("Deleting employee with ID: {}", id);
        return employeeService.deleteEmployee(id) ? ResponseEntity.ok(new ApiResponse<>(null))
                : buildNotFoundResponse("Employee not found");
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<Employee>>> searchEmployees(@RequestParam(required = false) String name,
            @RequestParam(required = false) String gender) {
        logger.info("Searching employees by name: {} and gender: {}", name, gender);
        List<Employee> employees = employeeService.searchEmployees(name, gender);
        return employees.isEmpty() ? buildNoContentResponse() : ResponseEntity.ok(new ApiResponse<>(employees));
    }

    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<Employee>>> getEmployeesWithPagination(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        logger.info("Fetching employees page: {}, size: {}, sorted by: {}", page, size, sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Employee> employeesPage = employeeService.getEmployeesWithPagination(pageRequest);
        return employeesPage.isEmpty() ? buildNoContentResponse() : ResponseEntity.ok(new ApiResponse<>(employeesPage));
    }

    @PostMapping("/sendEmail")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<String>> sendEmail(@RequestParam String to, @RequestParam String subject) {
        emailNotificationService.sendEmailWithTemplate(to, subject, subject);
        logger.info("Email sent to {} with subject {}", to, subject);
        return ResponseEntity.ok(new ApiResponse<>("Email sent successfully to " + to));
    }

    // Helper Methods
    private <T> ResponseEntity<ApiResponse<T>> buildNoContentResponse() {
        logger.info("No content to return. Returning 204 No Content");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse<>(null));
    }

    private <T> ResponseEntity<ApiResponse<T>> buildNotFoundResponse(String message) {
        logger.warn(message + " Returning 404 Not Found");
        ApiResponse<T> apiResponse = new ApiResponse<>(null);
        apiResponse.setMessage(message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    private <T> ResponseEntity<ApiResponse<T>> buildCreatedResponse(ApiResponse<T> response) {
        logger.info("Resource created successfully. Returning 201 Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}