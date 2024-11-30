package com.talenttrack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApiSecurityConfig  {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http.csrf().disable() // Disable CSRF for stateless JWT authentication
	        .authorizeRequests()
	            // Public endpoints: no authentication required
	            .antMatchers(
	                "/api/v1/auth/login",
	                "/api/v1/auth/register",
	                "/finance/realtime-quotes"
	            ).permitAll()
	            
	            // Admin-specific endpoints: restricted to ADMIN role
	            .antMatchers("/admin/**").hasRole("ADMIN")
	            
	            // User-specific endpoints: restricted to ADMIN or USER roles
	            .antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
	            
	            // Employee-related endpoints: specific endpoints require authentication
	         
	            
	            // Allow public access to other employee endpoints
	            .antMatchers("/api/v1/employees/**").permitAll()
	            
	            // All other requests must be authenticated
	            .anyRequest().authenticated()
	        .and()
	        // Set session management policy to stateless for JWT
	        .sessionManagement()
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	    return http.build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) 
	        throws Exception {
	    return authenticationConfiguration.getAuthenticationManager();
	}
}