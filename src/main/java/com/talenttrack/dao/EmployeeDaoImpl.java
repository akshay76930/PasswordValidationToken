package com.talenttrack.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public EmployeeDaoImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public List<Employee> findAll(String filterByName, String sortBy, String sortDirection, int page, int size) {
		logger.info("Fetching employees with filter: '{}', sort by: '{}', direction: '{}', page: {}, size: {}",
				filterByName, sortBy, sortDirection, page, size);

		if (sortBy == null || sortBy.isEmpty())
			sortBy = "id";
		if (sortDirection == null || sortDirection.isEmpty())
			sortDirection = "asc";

		Pageable pageable = PageRequest.of(page, size,
				"desc".equalsIgnoreCase(sortDirection) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

		// Use CriteriaBuilder to construct the query dynamically
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
		Root<Employee> root = query.from(Employee.class);

		// Apply filter if present
		Predicate predicate = cb.conjunction(); // Initial predicate is always true
		if (filterByName != null && !filterByName.isBlank()) {
			logger.debug("Applying name filter: '{}'", filterByName);
			predicate = cb.and(predicate, cb.like(cb.lower(root.get("name")), "%" + filterByName.toLowerCase() + "%"));
		}

		// Apply sorting
		if ("desc".equalsIgnoreCase(sortDirection)) {
			query.orderBy(cb.desc(root.get(sortBy)));
		} else {
			query.orderBy(cb.asc(root.get(sortBy)));
		}

		// Apply where clause
		query.where(predicate);

		// Execute the query
		TypedQuery<Employee> typedQuery = entityManager.createQuery(query);
		typedQuery.setFirstResult((int) pageable.getOffset());
		typedQuery.setMaxResults(pageable.getPageSize());

		List<Employee> employees = typedQuery.getResultList();

		logger.info("Total employees fetched: {}", employees.size());
		return employees;
	}

	@Override
	public Employee insertEmployee(@Valid Employee employee) {
		logger.info("Inserting new employee: {}", employee);

		// Check if an employee with the same email already exists
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Root<Employee> root = query.from(Employee.class);

		query.select(cb.count(root)).where(cb.equal(root.get("email"), employee.getEmail()));

		Long count = entityManager.createQuery(query).getSingleResult();

		if (count > 0) {
			logger.warn("Employee with email '{}' already exists!", employee.getEmail());
			throw new RuntimeException("Employee with email " + employee.getEmail() + " already exists.");
		}

		// Save the new employee
		return employeeRepository.save(employee);
	}

	@Override
	public boolean updateEmployee(@Valid Employee employee){
	    logger.info("Attempting to update employee with ID: {}", employee.getId());

	    if (!employeeRepository.existsById(employee.getId())) {
	        logger.warn("Employee with ID {} not found for update.", employee.getId());
	        throw new RuntimeException("Employee with ID " + employee.getId() + " not found.");
	    }

	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Long> query = cb.createQuery(Long.class);
	    Root<Employee> root = query.from(Employee.class);

	    if (employee.getId() != null) {
	        query.select(cb.count(root))
	             .where(cb.and(
	                 cb.equal(root.get("email"), employee.getEmail()),
	                 cb.notEqual(root.get("id"), employee.getId()) // Updated field and null check
	             ));
	    } else {
	        query.select(cb.count(root))
	             .where(cb.equal(root.get("email"), employee.getEmail()));
	    }

	    Long count = entityManager.createQuery(query).getSingleResult();
	    if (count > 0) {
	        logger.warn("Another employee with email '{}' already exists!", employee.getEmail());
	        throw new RuntimeException("Another employee with email " + employee.getEmail() + " already exists.");
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

	    // Check if the employee has active dependencies
	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Long> query = cb.createQuery(Long.class);
	    Root<Employee> root = query.from(Employee.class);

	    query.select(cb.count(root)).where(cb.equal(root.get("id"), employeeId)); // Updated field name
	    Long dependencyCount = entityManager.createQuery(query).getSingleResult();

	    if (dependencyCount > 0) {
	        logger.warn("Employee with ID {} cannot be deleted as they are assigned to active projects.", employeeId);
	        throw new RuntimeException("Cannot delete employee with ID " + employeeId + " as they are assigned to active projects.");
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
