package com.spring.project.DataValidation.CrudApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.spring.project.DataValidation.CrudApplication.Service.EmailNotificationService;

@SpringBootApplication
public class CrudApplication {

	

	public static void main(String[] args) {
		SpringApplication.run(CrudApplication.class, args);
	}
}