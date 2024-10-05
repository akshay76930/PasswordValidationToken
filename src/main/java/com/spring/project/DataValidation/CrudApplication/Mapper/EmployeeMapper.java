package com.spring.project.DataValidation.CrudApplication.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.spring.project.DataValidation.CrudApplication.Entity.Employee;

public class EmployeeMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getLong("id"));
        employee.setName(rs.getString("name"));
        employee.setContact(rs.getString("contact"));
        employee.setGender(rs.getString("gender"));
        employee.setEmail(rs.getString("email"));
        employee.setPassword(rs.getString("password")); // Be cautious with handling passwords!
        return employee;
    }
}
