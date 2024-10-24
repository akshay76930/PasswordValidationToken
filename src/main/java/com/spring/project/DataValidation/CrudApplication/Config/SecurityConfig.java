package com.spring.project.DataValidation.CrudApplication.Config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/api/forgot-password", "/api/reset-password").permitAll() // Allow access to these endpoints
                .anyRequest().authenticated()
            .and()
            .formLogin(); // Add other authentication methods as needed
    }
}
