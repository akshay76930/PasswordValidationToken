package com.spring.project.DataValidation.CrudApplication.Dao;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.project.DataValidation.CrudApplication.Entity.Employee;
import com.spring.project.DataValidation.CrudApplication.Repository.EmployeeRepository;

@Service
public class EmployeeDaoImpl implements EmployeeDao {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeDaoImpl.class);

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeDaoImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> findAll() {
        logger.info("Fetching all employees");
        return employeeRepository.findAll();
    }

    @Override
    public Employee insertEmployee(@Valid Employee employee) {
        logger.info("Inserting employee: {}", employee.getName());
        return employeeRepository.save(employee);
    }

    @Override
    public boolean updateEmployee(@Valid Employee employee) {
        if (employeeRepository.existsById(employee.getId())) {
            logger.info("Updating employee: {}", employee.getId());
            employeeRepository.save(employee);
            return true;
        } else {
            logger.warn("Employee with ID {} not found for update", employee.getId());
            return false;
        }
    }

    @Override
    public boolean deleteEmployee(Long employeeId) {
        if (employeeRepository.existsById(employeeId)) {
            logger.info("Deleting employee with ID: {}", employeeId);
            employeeRepository.deleteById(employeeId);
            return true;
        } else {
            logger.warn("Employee with ID {} not found for deletion", employeeId);
            return false;
        }
    }

    @Override
    public Optional<Employee> findById(Long id) {
        logger.info("Fetching employee with ID: {}", id);
        return employeeRepository.findById(id);
    }
}
