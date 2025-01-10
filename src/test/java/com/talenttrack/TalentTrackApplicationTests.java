package com.talenttrack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talenttrack.entity.Employee;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TalentTrackApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(TalentTrackApplicationTests.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllEmployees() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/employees/all"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        List<Employee> employees = List.of(objectMapper.readValue(responseBody, Employee[].class));

        assertThat(employees).isNotEmpty();
        logger.info("Fetched Employees: {}", employees);
    }

    @Test
    void testCreateEmployeeAPI() throws Exception {
        Employee newEmployee = new Employee();
        newEmployee.setName("John Doe");

        MvcResult result = mockMvc.perform(post("/api/v1/employees/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isCreated())
                .andReturn();

        Employee createdEmployee = objectMapper.readValue(result.getResponse().getContentAsString(), Employee.class);

        assertThat(createdEmployee).isNotNull();
        assertThat(createdEmployee.getId()).isNotNull();
        assertThat(createdEmployee.getName()).isEqualTo("John Doe");

        logger.info("Created Employee: {}", createdEmployee);
    }

    @Test
    void testUpdateEmployeeAPI() throws Exception {
        Employee updatedEmployee = new Employee();
        updatedEmployee.setName("Jane Doe");

        MvcResult result = mockMvc.perform(put("/api/v1/employees/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isOk())
                .andReturn();

        Employee employee = objectMapper.readValue(result.getResponse().getContentAsString(), Employee.class);

        assertThat(employee).isNotNull();
        assertThat(employee.getName()).isEqualTo("Jane Doe");

        logger.info("Updated Employee: {}", employee);
    }

    @Test
    void testDeleteEmployeeAPI() throws Exception {
        Long employeeIdToDelete = 1L;

        mockMvc.perform(delete("/api/v1/employees/delete/{id}", employeeIdToDelete))
                .andExpect(status().isOk());

        logger.info("Employee with ID {} deleted successfully.", employeeIdToDelete);

        mockMvc.perform(get("/api/v1/employees/{id}", employeeIdToDelete))
                .andExpect(status().isNotFound());

        logger.info("Confirmed that employee with ID {} no longer exists.", employeeIdToDelete);
    }
}
