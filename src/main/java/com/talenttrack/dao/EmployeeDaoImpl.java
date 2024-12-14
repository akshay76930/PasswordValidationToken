package com.talenttrack.dao;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.talenttrack.entity.Employee;
import com.talenttrack.repository.EmployeeRepository;

@Service
public class EmployeeDaoImpl implements EmployeeDao {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeDaoImpl.class);
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeDaoImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> findAll(String filterByName, String sortBy, String sortDirection, int page, int size) {
        logger.info("Fetching employees with filter: '{}', sort by: '{}', direction: '{}', page: {}, size: {}",
                filterByName, sortBy, sortDirection, page, size);

        if (sortBy == null || sortBy.isEmpty()) sortBy = "id";
        if (sortDirection == null || sortDirection.isEmpty()) sortDirection = "asc";

        Pageable pageable = PageRequest.of(
                page, 
                size, 
                "desc".equalsIgnoreCase(sortDirection) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending()
        );

        Page<Employee> employeePage;

        if (filterByName != null && !filterByName.isBlank()) {
            logger.debug("Applying name filter: '{}'", filterByName);
            employeePage = employeeRepository.findByNameContaining(filterByName.trim(), pageable);
        } else {
            logger.debug("No filter applied, fetching all employees.");
            employeePage = employeeRepository.findAll(pageable);
        }

        logger.info("Total employees fetched: {}", employeePage.getTotalElements());
        return employeePage.getContent();
    }

    @Override
    public Employee insertEmployee(@Valid Employee employee) {
        logger.info("Inserting new employee: {}", employee);
        return employeeRepository.save(employee);
    }

    @Override
    public boolean updateEmployee(@Valid Employee employee) {
        logger.info("Attempting to update employee with ID: {}", employee.getId());
        if (!employeeRepository.existsById(employee.getId())) {
            logger.warn("Employee with ID {} not found for update.", employee.getId());
            throw new RuntimeException("Employee with ID " + employee.getId() + " not found.");
        }
        employeeRepository.save(employee);
        logger.info("Successfully updated employee with ID: {}", employee.getId());
        return true;
    }

    @Override
    public boolean deleteEmployee(Long employeeId) {
        logger.info("Attempting to delete employee with ID: {}", employeeId);
        if (!employeeRepository.existsById(employeeId)) {
            logger.warn("Employee with ID {} not found for deletion.", employeeId);
            throw new RuntimeException("Employee with ID " + employeeId + " not found.");
        }
        employeeRepository.deleteById(employeeId);
        logger.info("Successfully deleted employee with ID: {}", employeeId);
        return true;
    }

    @Override
    public Optional<Employee> findById(Long id) {
        logger.info("Fetching employee with ID: {}", id);
        return employeeRepository.findById(id);
    }
}
