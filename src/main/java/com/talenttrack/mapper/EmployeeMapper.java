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

/**
 * RowMapper implementation that maps rows of the result set to Employee objects.
 * This class helps in converting the rows from the database into Employee entity objects.
 */
public class EmployeeMapper implements RowMapper<Employee> {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeMapper.class);

    /**
     * Maps each row of the ResultSet to an Employee entity.
     *
     * @param rs The ResultSet to be mapped.
     * @param rowNum The number of the current row (unused here).
     * @return A mapped Employee entity.
     * @throws SQLException if an error occurs while accessing the ResultSet.
     */
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();

        // Mapping each column in the ResultSet to the corresponding field in the Employee object
        employee.setId(rs.getLong("id"));
        employee.setName(rs.getString("name"));
        employee.setContact(rs.getString("contact"));
        employee.setGender(rs.getString("gender"));
        employee.setEmail(rs.getString("email"));

       
        return employee;
    }
}
