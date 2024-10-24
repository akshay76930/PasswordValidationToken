package com.spring.project.DataValidation.CrudApplication.Config;


import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .servers(Arrays.asList(
                new Server().url("http://localhost:8080").description("Local development server"),
                new Server().url("https://api.production.com").description("Production server")
            ))
            .info(new Info()
                .title("Employee Management API")
                .version("1.0.0")
                .description("This API allows CRUD operations on the employee database. It provides endpoints for managing employees in the system, including adding, updating, and deleting employee records.")
                .termsOfService("https://example.com/terms")
                .contact(new Contact()
                    .name("Support Team")
                    .url("https://example.com/support")
                    .email("support@example.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://springdoc.org")))
            .components(new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
    }
}
