package com.spring.project.DataValidation.CrudApplication.Service;

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

import com.spring.project.DataValidation.CrudApplication.Entity.Employee;
import com.spring.project.DataValidation.CrudApplication.Exception.EmailAlreadyExistsException;
import com.spring.project.DataValidation.CrudApplication.Exception.InvalidPasswordException;
import com.spring.project.DataValidation.CrudApplication.Repository.EmployeeRepository;

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

        // Validate the password
        String password = Optional.ofNullable(employee.getPassword()).filter(p -> !p.isEmpty()).orElse(DEFAULT_PASSWORD);

        if (!isValidPassword(password)) {
            logger.error("Invalid password for employee: {}", employee.getName());
            throw new InvalidPasswordException("Password must be at least 8 characters long, contain at least one letter, one number, and one special character.");
        }

        employee.setPassword(password); // Set the password

        // Check if the email is already in use
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

        // Fetch the existing employee
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));

        // Check if the new email is unique
        if (!existingEmployee.getEmail().equals(employee.getEmail()) &&
            employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            logger.error("Email already exists: {}", employee.getEmail());
            throw new EmailAlreadyExistsException("Email address already in use.");
        }

        // Validate password
        if (!isValidPassword(employee.getPassword())) {
            logger.error("Invalid password for employee: {}", employee.getName());
            throw new InvalidPasswordException("Invalid password format.");
        }

        // Update employee details
        existingEmployee.setName(employee.getName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setGender(employee.getGender());
        existingEmployee.setPassword(employee.getPassword());

        employeeRepository.save(existingEmployee); // Save the updated employee
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
        // Start with a base query or repository call
        if (name == null && gender == null) {
            // Both parameters are null, so return all employees
            return employeeRepository.findAll();
        } else if (name != null && gender == null) {
            // Only name is provided
            return employeeRepository.findByNameContainingIgnoreCase(name);
        } else if (name == null && gender != null) {
            // Only gender is provided
            return employeeRepository.findByGenderIgnoreCase(gender);
        } else {
            // Both name and gender are provided
            return employeeRepository.findByNameContainingIgnoreCaseAndGenderIgnoreCase(name, gender);
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        logger.info("Fetching all employees");
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        logger.info("Fetching employee with ID: {}", id);
        return employeeRepository.findById(id);
    }

    @Override
    public Page<Employee> getEmployeesWithPagination(Pageable pageable) {
        logger.info("Fetching paginated list of employees");
        return employeeRepository.findAll(pageable);
    }

    private boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    @Override
    public Page<Employee> getEmployeesWithPagination(PageRequest pageRequest) {
        logger.info("Fetching paginated list of employees with PageRequest");
        return employeeRepository.findAll(pageRequest);
    }
}
