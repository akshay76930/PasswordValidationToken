package com.spring.project.DataValidation.CrudApplication.Config;

import java.util.Arrays;
import java.util.List;

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
        // Define the servers list
        List<Server> servers = Arrays.asList(
            new Server().url("http://localhost:8080").description("Local Development Server"),
            new Server().url("https://api.production.com").description("Production Server")
        );

        return new OpenAPI()
            .servers(servers)
            .info(new Info()
                .title("Employee Management API")
                .version("1.0.0")
                .description("API for performing CRUD operations on employee records, including adding, updating, and deleting records.")
                .termsOfService("https://example.com/terms")
                .contact(new Contact()
                    .name("Support Team")
                    .url("https://example.com/support")
                    .email("support@example.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
            )
            .components(new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT"))
            );
    }
}
