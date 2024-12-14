/**
 * This class is responsible for mapping a row from the ResultSet to an Employee object.
 * It implements the RowMapper interface, which is used by Spring JDBC to convert each row 
 * of a result set into an object.
 * 
 * <p>Author: Akshay Dhere &lt;akshaydhere14@gmail.com&gt;</p>
 */

package com.talenttrack.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.talenttrack.entity.Employee;


public class EmployeeMapper implements RowMapper<Employee> {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeMapper.class);

    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();

        try {
            logger.debug("Mapping row number: {}", rowNum);
 
            employee.setId(rs.getLong("id"));
            employee.setName(rs.getString("name"));
            employee.setContact(rs.getString("contact"));
            employee.setGender(rs.getString("gender"));
            employee.setEmail(rs.getString("email"));

        } catch (SQLException e) {
            logger.error("Error mapping row number {}: {}", rowNum, e.getMessage());
            throw e; // Re-throwing the exception for further handling
        }

        logger.debug("Successfully mapped row number: {}", rowNum);
        return employee;
    }
}
