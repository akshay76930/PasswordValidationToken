package com.spring.project.DataValidation.CrudApplication.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.spring.project.DataValidation.CrudApplication.Entity.Employee;

/**
 * This class implements RowMapper interface to map the rows of a ResultSet
 * to an Employee entity.
 */
public class EmployeeMapper implements RowMapper<Employee> {
    
    /**
     * Maps a single row of the ResultSet to an Employee object.
     *
     * @param rs     the ResultSet to map (pre-initialized for the current row)
     * @param rowNum the number of the current row (starting from 0)
     * @return an Employee object populated with data from the ResultSet
     * @throws SQLException if a SQLException is encountered getting column values
     */
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();
        
        employee.setId(rs.getLong("id"));
        employee.setName(rs.getString("name"));
        employee.setContact(rs.getString("contact"));
        employee.setGender(rs.getString("gender"));
        employee.setEmail(rs.getString("email"));

        // Password handling: Ensure this is hashed in your entity or retrieved securely
        String password = rs.getString("password");
        if (password != null) {
            employee.setPassword(password); // Placeholder - use secure handling in practice
        }

        return employee;
    }
}
