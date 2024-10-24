package com.spring.project.DataValidation.CrudApplication.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.project.DataValidation.CrudApplication.Dao.EmployeeDao;
import com.spring.project.DataValidation.CrudApplication.Entity.Employee;
import com.spring.project.DataValidation.CrudApplication.Service.Repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private static final String DEFAULT_PASSWORD = "SecureDefaultPassword123!";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*])(?=.{8,})");

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Resource
    private final EmployeeDao employeeDao;
    
    @Autowired
    private EmailNotificationService emailNotificationService;

    public EmployeeServiceImpl(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public List<Employee> findAll() {
        logger.info("Fetching all employees");
        return employeeDao.findAll();
    }

    @Override
    @Transactional
    public Employee insertEmployee(Employee employee) {
        logger.info("Inserting a new employee: {}", employee.getName());

        // Validate the provided password
        String password = Optional.ofNullable(employee.getPassword()).filter(p -> !p.isEmpty() && isValidPassword(p))
                .orElseGet(() -> {
                    logger.warn("Invalid or no password provided, assigning default password");
                    return DEFAULT_PASSWORD;
                });

        employee.setPassword(password);

        // Check for existing employee with the same email
        if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            throw new RuntimeException("Email address already in use");
        }

        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public Employee updateEmployee(Employee employee) {
        logger.info("Updating employee with ID: {}", employee.getId());
        try {
            employeeDao.updateEmployee(employee);
            logger.info("Successfully updated employee with ID: {}", employee.getId());
            return employee;
        } catch (Exception e) {
            logger.error("Failed to update employee with ID: {}", employee.getId(), e);
            throw new RuntimeException("Failed to update employee", e);
        }
    }

    @Override
    @Transactional
    public boolean deleteEmployee(Long employeeId) {
        logger.info("Deleting employee with ID: {}", employeeId);
        try {
            boolean isDeleted = employeeDao.deleteEmployee(employeeId);
            if (isDeleted) {
                logger.info("Successfully deleted employee with ID: {}", employeeId);
                return true;
            } else {
                logger.warn("Employee with ID: {} not found for deletion", employeeId);
                return false;
            }
        } catch (Exception e) {
            logger.error("Failed to delete employee with ID: {}", employeeId, e);
            return false;
        }
    }

    @Override
    public Optional<Employee> findById(Long id) {
        logger.info("Fetching employee with ID: {}", id);
        return employeeDao.findById(id);
    }

    private boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

}
