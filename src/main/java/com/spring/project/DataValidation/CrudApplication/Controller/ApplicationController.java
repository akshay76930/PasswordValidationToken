package com.spring.project.DataValidation.CrudApplication.Controller;

import java.util.List;

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
import com.spring.project.DataValidation.CrudApplication.Service.EmailNotificationService;
import com.spring.project.DataValidation.CrudApplication.Service.EmployeeService;
import com.spring.project.DataValidation.CrudApplication.Response.ApiResponse;

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
        logInfo("Fetching all employees");
        List<Employee> employees = employeeService.findAll();
        return buildResponse(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Employee>> getEmployeeById(@PathVariable Long id) {
        logInfo("Fetching employee with ID: {}", id);
        Employee employee = employeeService.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return ResponseEntity.ok(new ApiResponse<>(employee));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Employee>> insertEmployee(@Valid @RequestBody Employee employee) {
        logInfo("Inserting a new employee: {}", employee.getName());
        Employee savedEmployee = employeeService.insertEmployee(employee);
        return buildCreatedResponse(new ApiResponse<>(savedEmployee));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Void>> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employee) {
        logInfo("Updating employee with ID: {}", id);
        employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(new ApiResponse<>(null));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        logInfo("Deleting employee with ID: {}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(new ApiResponse<>(null));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Employee>>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String gender) {
        logInfo("Searching employees by name: {} and gender: {}", name, gender);
        List<Employee> employees = employeeService.searchEmployees(name, gender);
        return buildResponse(employees);
    }

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<Employee>>> getEmployeesWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        logInfo("Fetching employees page: {}, size: {}, sorted by: {}", page, size, sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Employee> employeesPage = employeeService.findAllWithPagination(pageRequest);
        return ResponseEntity.ok(new ApiResponse<>(employeesPage));
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<ApiResponse<String>> sendEmail(@RequestParam String to, @RequestParam String subject) {
        emailNotificationService.sendEmailWithTemplate(to, subject);
        logInfo("Email sent to {}", to);
        return ResponseEntity.ok(new ApiResponse<>("Email sent successfully to " + to));
    }

    // Helper Methods
    private void logInfo(String message, Object... params) {
        if (logger.isInfoEnabled()) {
            logger.info(message, params);
        }
    }

    private <T> ResponseEntity<ApiResponse<T>> buildNotFoundResponse(String message) {
        logInfo(message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(message));
    }

    private <T> ResponseEntity<ApiResponse<T>> buildCreatedResponse(ApiResponse<T> response) {
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private <T> ResponseEntity<ApiResponse<List<T>>> buildResponse(List<T> list) {
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(new ApiResponse<>(list));
    }
}
