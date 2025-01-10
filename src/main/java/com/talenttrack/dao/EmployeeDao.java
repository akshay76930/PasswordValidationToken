package com.talenttrack.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.talenttrack.entity.Employee;

@Repository
public interface EmployeeDao {

    List<Employee> findAll(String filterByName, String sortBy, String sortDirection, int page, int size);

    Employee insertEmployee(Employee employee);

    boolean updateEmployee(Employee employee);

    boolean deleteEmployee(Long employeeId);

    Optional<Employee> findById(Long id);

}
