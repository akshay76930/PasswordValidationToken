package com.spring.project.DataValidation.CrudApplication.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers(
                    "/api/v1/auth/login", 
                    "/api/v1/auth/register", 
                    "/api/v1/auth/test-token", 
                    "/api/forgot-password", 
                    "/api/reset-password", 
                    "/api/v1/employees/all", 
                    "/api/v1/employees/search", 
                    "/api/v1/employees/page"
                ).permitAll()
                .antMatchers(
                    "/api/v1/employees/sendEmail", 
                    "/api/v1/employees/create", 
                    "/api/v1/employees/update/**", 
                    "/api/v1/employees/delete/**"
                ).authenticated()
                .anyRequest().authenticated()
            .and()
            .httpBasic();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
