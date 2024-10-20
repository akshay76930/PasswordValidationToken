package com.spring.project.DataValidation.CrudApplication.Dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.spring.project.DataValidation.CrudApplication.Entity.Employee;
import com.spring.project.DataValidation.CrudApplication.Mapper.EmployeeMapper;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeDaoImpl.class);

    private final NamedParameterJdbcTemplate template;
    private final JdbcTemplate jdbcTemplate;

    public EmployeeDaoImpl(NamedParameterJdbcTemplate template, JdbcTemplate jdbcTemplate) {
        this.template = template;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Employee> findAll() {
        final String sql = "SELECT * FROM employee";
        logger.info("Executing query: {}", sql);
        return template.query(sql, new EmployeeMapper());
    }

    @Override
    public boolean insertEmployee(Employee employee) {
        final String sql = "INSERT INTO employee (name, contact, gender, email, password) " +
                "VALUES (:name, :contact, :gender, :email, :password)";
        
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", employee.getName())
                .addValue("contact", employee.getContact())
                .addValue("gender", employee.getGender())
                .addValue("email", employee.getEmail())
                .addValue("password", employee.getPassword());
        
        KeyHolder holder = new GeneratedKeyHolder();
        int rowsAffected = template.update(sql, param, holder);

        logger.info("Inserted employee: {}, Rows affected: {}", employee.getName(), rowsAffected);
        return rowsAffected > 0;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        final String sql = "UPDATE employee SET name = :name, email = :email, contact = :contact, gender = :gender " +
                "WHERE id = :id";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", employee.getId())
                .addValue("name", employee.getName())
                .addValue("contact", employee.getContact())
                .addValue("email", employee.getEmail())
                .addValue("gender", employee.getGender());

        int rowsAffected = template.update(sql, param);

        logger.info("Updated employee with ID: {}, Rows affected: {}", employee.getId(), rowsAffected);
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteEmployee(Long employeeId) {
        final String sql = "DELETE FROM employee WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", employeeId);

        int rowsAffected = template.update(sql, params);
        logger.info("Deleted employee with ID: {}, Rows affected: {}", employeeId, rowsAffected);
        return rowsAffected > 0;
    }

    @Override
    public Optional<Employee> findById(Long id) {
        final String sql = "SELECT * FROM employee WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        List<Employee> employees = template.query(sql, params, new EmployeeMapper());
        // Refactor with Java 8 Optional and Stream
        return employees.stream().findFirst();
    }
}
