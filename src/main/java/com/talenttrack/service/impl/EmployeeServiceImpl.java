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
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*])(?=.{8,})");

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public Employee insertEmployee(Employee employee) throws EmailAlreadyExistsException, InvalidPasswordException {
        logger.info("Inserting a new employee: {}", employee.getName());
        String password = Optional.ofNullable(employee.getPassword()).filter(p -> !p.isEmpty()).orElse(DEFAULT_PASSWORD);
        if (!isValidPassword(password)) {
            logger.error("Invalid password for employee: {}", employee.getName());
            throw new InvalidPasswordException("Password must be at least 8 characters long, contain at least one letter, one number, and one special character.");
        }
        employee.setPassword(password);
        if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            logger.error("Email already exists: {}", employee.getEmail());
            throw new EmailAlreadyExistsException("Email address already in use.");
        }
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public boolean updateEmployee(Long id, Employee employee) throws EmailAlreadyExistsException, InvalidPasswordException {
        logger.info("Updating employee with ID: {}", id);
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
        if (!existingEmployee.getEmail().equals(employee.getEmail()) &&
            employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            logger.error("Email already exists: {}", employee.getEmail());
            throw new EmailAlreadyExistsException("Email address already in use.");
        }
        if (!isValidPassword(employee.getPassword())) {
            logger.error("Invalid password for employee: {}", employee.getName());
            throw new InvalidPasswordException("Invalid password format.");
        }
        existingEmployee.setName(employee.getName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setGender(employee.getGender());
        existingEmployee.setPassword(employee.getPassword());
        employeeRepository.save(existingEmployee);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteEmployee(Long employeeId) {
        logger.info("Deleting employee with ID: {}", employeeId);
        if (employeeRepository.existsById(employeeId)) {
            employeeRepository.deleteById(employeeId);
            logger.info("Employee with ID: {} deleted successfully.", employeeId);
            return true;
        } else {
            logger.warn("Employee with ID: {} not found for deletion.", employeeId);
            return false;
        }
    }

    @Override
    public List<Employee> searchEmployees(String name, String gender) {
        if (name == null && gender == null) {
            return employeeRepository.findAll();
        } else if (name != null && gender == null) {
            return employeeRepository.findByNameContainingIgnoreCase(name);
        } else if (name == null && gender != null) {
            return employeeRepository.findByGenderIgnoreCase(gender);
        } else {
            return employeeRepository.findByNameContainingIgnoreCaseAndGenderIgnoreCase(name, gender);
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        logger.debug("Starting process to fetch all employees");
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            logger.warn("No employees found in the database");
        } else {
            logger.info("Fetched {} employees successfully", employees.size());
        }
        return employees;
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        if (id == null) {
            logger.warn("Employee ID provided is null");
            throw new IllegalArgumentException("Employee ID cannot be null");
        }
        logger.debug("Attempting to fetch employee with ID: {}", id);
        return employeeRepository.findById(id)
                .map(employee -> {
                    logger.info("Employee found with ID: {}", id);
                    return employee;
                })
                .or(() -> {
                    logger.warn("No employee found with ID: {}", id);
                    return Optional.empty();
                });
    }

    @Override
    public Page<Employee> getEmployeesWithPagination(Pageable pageable) {
        if (pageable == null) {
            logger.warn("Pageable object is null");
            throw new IllegalArgumentException("Pageable cannot be null");
        }
        logger.debug("Fetching paginated employees using: {}", pageable);
        Page<Employee> employeesPage = employeeRepository.findAll(pageable);
        logger.info("Fetched {} employees in {} pages", employeesPage.getTotalElements(), employeesPage.getTotalPages());
        return employeesPage;
    }

    @Override
    public Page<Employee> getEmployeesWithPagination(PageRequest pageRequest) {
        if (pageRequest == null) {
            logger.warn("PageRequest object is null");
            throw new IllegalArgumentException("PageRequest cannot be null");
        }
        logger.debug("Fetching paginated employees using PageRequest: {}", pageRequest);
        return getEmployeesWithPagination((Pageable) pageRequest);
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            logger.warn("Password validation failed: null or empty");
            return false;
        }
        boolean isValid = PASSWORD_PATTERN.matcher(password).matches();
        logger.debug("Password validation result for '{}': {}", password, isValid);
        return isValid;
    }
}
