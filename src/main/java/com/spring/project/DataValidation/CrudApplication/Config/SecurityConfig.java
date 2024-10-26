package com.spring.project.DataValidation.CrudApplication.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disable CSRF for simplicity; consider enabling it based on your requirements
            .authorizeRequests()
                .antMatchers("/api/v1/employees/all").permitAll() // Allow unauthenticated access to the "Get all employees" endpoint
                .antMatchers("/api/v1/auth/login").permitAll() // Allow login endpoint without authentication
                .antMatchers("/api/v1/auth/register").permitAll() // Allow login endpoint without authentication
                .antMatchers("/api/v1/employees/search").permitAll() // Allow unauthenticated access to the "Search employees" endpoint
                .antMatchers("/api/forgot-password", "/api/reset-password").permitAll() ////Allow access to password reset functionalities without requiring authentication.
                .antMatchers("/api/v1/employees/page").permitAll() // Allow unauthenticated access to paginated list
                .antMatchers("/api/v1/employees/sendEmail").authenticated() // Restrict "Send email" to authenticated users
                .antMatchers("/api/v1/employees/create").authenticated() // Restrict employee creation to authenticated users
                
                .antMatchers("/api/v1/employees/update/**").authenticated() // Restrict employee updates to authenticated users
                .antMatchers("/api/v1/employees/delete/**").authenticated() // Restrict employee deletion to authenticated users
                .anyRequest().authenticated() // Require authentication for all other requests
            .and()
            .httpBasic(); // Enable basic authentication; configure as needed
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}









