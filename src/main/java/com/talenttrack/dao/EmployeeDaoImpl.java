package com.talenttrack.dao;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
		boolean exists = employeeRepository.existsById(employee.getId());
		if (exists) {
			logger.info("Updating employee: {}", employee.getId());
			employeeRepository.save(employee);
		} else {
			logger.warn("Employee with ID {} not found for update", employee.getId());
		}
		return exists;
	}

	@Override
	public boolean deleteEmployee(Long employeeId) {
		boolean exists = employeeRepository.existsById(employeeId);
		if (exists) {
			logger.info("Deleting employee with ID: {}", employeeId);
			employeeRepository.deleteById(employeeId);
		} else {
			logger.warn("Employee with ID {} not found for deletion", employeeId);
		}
		return exists;
	}

	@Override
	public Optional<Employee> findById(Long id) {
		logger.info("Fetching employee with ID: {}", id);
		return employeeRepository.findById(id);
	}
}
