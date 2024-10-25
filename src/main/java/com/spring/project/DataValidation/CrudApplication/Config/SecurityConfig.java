package com.spring.project.DataValidation.CrudApplication.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(authorize -> authorize
                .antMatchers("/api/forgot-password", "/api/reset-password").permitAll() // Public endpoints
                .anyRequest().authenticated() // Secure all other endpoints
            )
            .formLogin(form -> form
                .loginPage("/login") // Custom login page (optional)
                .permitAll() // Allow everyone to see the login page
                .defaultSuccessUrl("/home", true) // Redirect after successful login
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // Customize the logout URL
                .logoutSuccessUrl("/login?logout") // Redirect to login with a logout message
                .permitAll()
            );

        return http.build();
    }
}
