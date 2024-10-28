package com.spring.project.DataValidation.CrudApplication.Controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.spring.project.DataValidation.CrudApplication.Entity.Employee;
import com.spring.project.DataValidation.CrudApplication.Service.EmailNotificationService;
import com.spring.project.DataValidation.CrudApplication.Service.EmployeeService;

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

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        logger.info("Fetching all employees");
        List<Employee> employees = employeeService.findAll();
        return employees.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        logger.info("Fetching employee with ID: {}", id);
        return employeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<Employee> insertEmployee(@Valid @RequestBody Employee employee) {
        logger.info("Inserting a new employee: {}", employee.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.insertEmployee(employee));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employee) {
        logger.info("Updating employee with ID: {}", id);
        employee.setId(id);
        return employeeService.updateEmployee(employee) != null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        logger.info("Deleting employee with ID: {}", id);
        return employeeService.deleteEmployee(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(@RequestParam(required = false) String name,
                                                          @RequestParam(required = false) String gender) {
        logger.info("Searching employees by name: {} and gender: {}", name, gender);
        List<Employee> employees = employeeService.searchEmployees(name, gender);
        return employees.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(employees);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Employee>> getEmployeesWithPagination(@RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(defaultValue = "id") String sortBy) {
        logger.info("Fetching employees page: {}, size: {}, sorted by: {}", page, size, sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(employeeService.findAllWithPagination(pageRequest));
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<String> sendEmail(@RequestParam String to, @RequestParam String subject) {
        emailNotificationService.sendEmailWithTemplate(to, subject);
        logger.info("Email sent to {}", to);
        return ResponseEntity.ok("Email sent successfully to " + to);
    }
}
