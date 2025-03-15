package com.talenttrack.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.talenttrack.entity.Employee;
import com.talenttrack.exception.EmailAlreadyExistsException;
import com.talenttrack.exception.InvalidPasswordException;
import com.talenttrack.repository.EmployeeRepository;
import com.talenttrack.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private static final String DEFAULT_PASSWORD = "SecureDefaultPassword123!";
    private static final Pattern PASSWORD_PATTERN = 
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public Employee insertEmployee(Employee employee) throws EmailAlreadyExistsException, InvalidPasswordException {
        logger.info("Inserting a new employee: {}", employee.getName());
        
        // Default password handling and validation
        String password = Optional.ofNullable(employee.getPassword())
                                  .filter(p -> !p.isEmpty())
                                  .orElse(DEFAULT_PASSWORD);
        validatePassword(password);
        
        // Email validation
        if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            logger.error("Email already exists: {}", employee.getEmail());
            throw new EmailAlreadyExistsException("Email address already in use.");
        }
        
        employee.setPassword(passwordEncoder.encode(password));
        return employeeRepository.save(employee);
    }

    private void validatePassword(String password) throws InvalidPasswordException {
        if (password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$")) {
            logger.debug("Password appears to be already encoded; skipping validation.");
            return;
        }
        
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            logger.error("Invalid password format for password: {}", password);
            throw new InvalidPasswordException("Password must be at least 8 characters long, contain at least one letter, one number, and one special character.");
        }
    }


    @Override
    @Transactional
    public Optional<Employee> updateEmployee(Long id, Employee employee) throws EmailAlreadyExistsException, InvalidPasswordException {
        logger.info("Updating employee with ID: {}", id);

        // Fetch existing employee
        Employee existingEmployee = findEmployeeById(id);

        // Email validation
        if (!existingEmployee.getEmail().equals(employee.getEmail()) &&
            employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            logger.error("Email already exists: {}", employee.getEmail());
            throw new EmailAlreadyExistsException("Email address already in use.");
        }

        // Password validation
        validatePassword(employee.getPassword());

        existingEmployee.setName(employee.getName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setGender(employee.getGender());
        existingEmployee.setPassword(passwordEncoder.encode(employee.getPassword()));
        
        employeeRepository.save(existingEmployee);
        logger.info("Successfully updated employee with ID: {}", id);
        return Optional.of(existingEmployee);
    }

    @Override
    @Transactional
    public Optional<Employee> deleteEmployee(Long employeeId) {
        logger.info("Attempting to delete employee with ID: {}", employeeId);
        Employee existingEmployee = findEmployeeById(employeeId);

        if (existingEmployee != null) {
            employeeRepository.deleteById(employeeId);
            logger.info("Employee with ID: {} deleted successfully.", employeeId);
            return Optional.of(existingEmployee);
        } else {
            logger.warn("Employee with ID: {} not found for deletion.", employeeId);
            return Optional.empty();
        }
    }

    @Override
    public List<Employee> searchEmployees(String name, String gender) {
        logger.debug("Searching employees with name: {}, gender: {}", name, gender);

        if (name != null && gender != null) {
            return employeeRepository.findByNameContainingIgnoreCaseAndGenderIgnoreCase(name, gender);
        } else if (name != null) {
            return employeeRepository.findByNameContainingIgnoreCase(name);
        } else if (gender != null) {
            return employeeRepository.findByGenderIgnoreCase(gender);
        } else {
            return employeeRepository.findAll();
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        logger.debug("Fetching all employees");
        List<Employee> employees = employeeRepository.findAll();
        
        if (employees.isEmpty()) {
            logger.warn("No employees found");
        } else {
            logger.info("Fetched {} employees", employees.size());
        }
        
        return employees;
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        logger.debug("Fetching employee by ID: {}", id);
        return Optional.ofNullable(findEmployeeById(id));
    }

    @Override
    public Page<Employee> getEmployeesWithPagination(Pageable pageable) {
        logger.debug("Fetching employees with pagination: {}", pageable);
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Page<Employee> getEmployeesWithPagination(PageRequest pageRequest) {
        return getEmployeesWithPagination((Pageable) pageRequest);
    }

    // Helper method to fetch employee by ID (with error handling)
    private Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> {
            logger.error("Employee not found with ID: {}", id);
            return new RuntimeException("Employee not found with ID: " + id);
        });
    }
}
