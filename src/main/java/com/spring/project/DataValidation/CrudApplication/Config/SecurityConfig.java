package com.spring.project.DataValidation.CrudApplication.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disable CSRF for simplicity in this example
            .authorizeRequests()
            .antMatchers("/api/forgot-password", "/api/reset-password").permitAll() // Allow access to these endpoints
            .anyRequest().authenticated() // All other requests require authentication
            .and()
            .httpBasic(); // Enable basic authentication
    }
}