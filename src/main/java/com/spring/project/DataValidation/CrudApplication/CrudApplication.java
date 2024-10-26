package com.spring.project.DataValidation.CrudApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.spring.project.DataValidation.CrudApplication.Repository")
@EntityScan("com.spring.project.DataValidation.CrudApplication.Entity")
public class CrudApplication {

	public static void main(String[] args) {
	  
		SpringApplication.run(CrudApplication.class, args);
	}
}