package com.spring.project.DataValidation.CrudApplication.Controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.project.DataValidation.CrudApplication.Entity.Employee;
import com.spring.project.DataValidation.CrudApplication.Response.ApiResponse;
import com.spring.project.DataValidation.CrudApplication.Service.EmailNotificationService;
import com.spring.project.DataValidation.CrudApplication.Service.EmployeeService;

@RestController
@RequestMapping("/api/v1/employees")
public class ApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
    private final EmployeeService employeeService;
    private final EmailNotificationService emailNotificationService;

    @Autowired
    public ApplicationController(EmployeeService employeeService, EmailNotificationService emailNotificationService) {
        this.employeeService = employeeService;
        this.emailNotificationService = emailNotificationService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Employee>>> getAllEmployees() {
        logger.info("Fetching all employees");
        List<Employee> employees = employeeService.getAllEmployees();
        return employees.isEmpty()
                ? buildNoContentResponse()
                : ResponseEntity.ok(new ApiResponse<>(employees));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Employee>> getEmployeeById(@PathVariable Long id) {
        logger.info("Fetching employee with ID: {}", id);
        return employeeService.getEmployeeById(id)
                .map(employee -> ResponseEntity.ok(new ApiResponse<>(employee)))
                .orElseGet(() -> buildNotFoundResponse("Employee not found"));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Employee>> insertEmployee(@Valid @RequestBody Employee employee) {
        logger.info("Inserting a new employee: {}", employee.getName());
        Employee savedEmployee = employeeService.insertEmployee(employee);
        return buildCreatedResponse(new ApiResponse<>(savedEmployee));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Void>> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employee) {
        logger.info("Updating employee with ID: {}", id);
        return employeeService.updateEmployee(id, employee)
                ? ResponseEntity.ok(new ApiResponse<>(null))  
                : buildNotFoundResponse("Employee not found");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        logger.info("Deleting employee with ID: {}", id);
        return employeeService.deleteEmployee(id)
                ? ResponseEntity.ok(new ApiResponse<>(null))  
                : buildNotFoundResponse("Employee not found");
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Employee>>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String gender) {
        logger.info("Searching employees by name: {} and gender: {}", name, gender);
        List<Employee> employees = employeeService.searchEmployees(name, gender);
        return employees.isEmpty()
                ? buildNoContentResponse()
                : ResponseEntity.ok(new ApiResponse<>(employees));
    }

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<Employee>>> getEmployeesWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        logger.info("Fetching employees page: {}, size: {}, sorted by: {}", page, size, sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Employee> employeesPage = employeeService.getEmployeesWithPagination(pageRequest);
        return employeesPage.isEmpty()
                ? buildNoContentResponse()
                : ResponseEntity.ok(new ApiResponse<>(employeesPage));
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<ApiResponse<String>> sendEmail(@RequestParam String to, @RequestParam String subject) {
        emailNotificationService.sendEmailWithTemplate(to, subject);
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
