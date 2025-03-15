package com.talenttrack.controller;

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

import com.talenttrack.entity.Employee;
import com.talenttrack.entity.User;
import com.talenttrack.repository.UserRepository;
import com.talenttrack.response.ApiResponse;
import com.talenttrack.response.ApiResponseFactory;
import com.talenttrack.service.EmailSenderService;
import com.talenttrack.service.EmployeeService;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	private final EmployeeService employeeService;
	private final EmailSenderService emailNotificationService;
	private UserRepository userRepository;

	@Autowired
	public EmployeeController(EmployeeService employeeService, EmailSenderService emailNotificationService) {
		this.employeeService = employeeService;
		this.emailNotificationService = emailNotificationService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Employee>> getEmployeeById(@PathVariable Long id) {
		logger.info("Fetching employee with ID: {}", id);
		Optional<Employee> employee = employeeService.getEmployeeById(id);
		return employee.map(emp -> new ResponseEntity<>(ApiResponseFactory.buildSuccessResponse(emp), HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(ApiResponseFactory.buildErrorResponse("Employee not found"),
						HttpStatus.NOT_FOUND));
	}

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<Employee>> insertEmployee(@Valid @RequestBody Employee employee,
			@RequestParam Long userId) {
		logger.info("Creating new employee: {}", employee.getName());

		// Ensure the user is set
		Optional<User> userOptional = userRepository.findById(userId);
		if (!userOptional.isPresent()) {
			return new ResponseEntity<>(ApiResponseFactory.buildErrorResponse("User not found"),
					HttpStatus.BAD_REQUEST);
		}

		employee.setUser(userOptional.get());
		Employee savedEmployee = employeeService.insertEmployee(employee);
		return new ResponseEntity<>(ApiResponseFactory.buildSuccessResponse(savedEmployee), HttpStatus.CREATED);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<ApiResponse<Employee>> updateEmployee(@PathVariable Long id,
			@Valid @RequestBody Employee employee) {
		logger.info("Updating employee with ID: {}", id);

		Optional<Employee> updatedEmployee = employeeService.updateEmployee(id, employee);
		return updatedEmployee
				.map(emp -> new ResponseEntity<>(ApiResponseFactory.buildSuccessResponse(emp), HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(ApiResponseFactory.buildErrorResponse("Employee not found"),
						HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
		logger.info("Attempting to delete employee with ID: {}", id);

		Optional<Employee> deletedEmployee = employeeService.deleteEmployee(id);
		if (deletedEmployee.isPresent()) {
			logger.info("Successfully deleted employee with ID: {}", id);
			return new ResponseEntity<>(ApiResponseFactory.buildSuccessResponse(null), HttpStatus.OK);
		} else {
			logger.warn("Employee with ID: {} not found", id);
			return new ResponseEntity<>(ApiResponseFactory.buildErrorResponse("Employee not found"),
					HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<List<Employee>>> searchEmployees(@RequestParam(required = false) String name,
			@RequestParam(required = false) String gender) {
		logger.info("Searching employees by name: '{}' and gender: '{}'", name, gender);
		List<Employee> employees = employeeService.searchEmployees(name, gender);
		return new ResponseEntity<>(ApiResponseFactory.buildSuccessResponse(employees), HttpStatus.OK);
	}

	@GetMapping("/page")
	public ResponseEntity<ApiResponse<Page<Employee>>> getEmployeesWithPagination(
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy) {
		logger.info("Fetching employees page: {}, size: {}, sorted by: {}", page, size, sortBy);
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
		Page<Employee> employeesPage = employeeService.getEmployeesWithPagination(pageRequest);
		return new ResponseEntity<>(ApiResponseFactory.buildSuccessResponse(employeesPage), HttpStatus.OK);
	}

	@PostMapping("/sendEmail")
	public ResponseEntity<ApiResponse<String>> sendEmail(@RequestParam String to, @RequestParam String subject) {
		logger.info("Sending email to: {} with subject: {}", to, subject);
		emailNotificationService.sendEmailWithTemplate(to, subject, subject);
		return new ResponseEntity<>(ApiResponseFactory.buildSuccessResponse("Email sent successfully to " + to),
				HttpStatus.OK);
	}
}
