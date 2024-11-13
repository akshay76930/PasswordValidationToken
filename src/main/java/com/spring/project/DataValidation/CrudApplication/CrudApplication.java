package com.spring.project.DataValidation.CrudApplication;

import java.io.PrintStream;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CrudApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(CrudApplication.class);

        // Customize the application banner
        application.setBanner(new CustomBanner());

        // Run the application
        application.run(args);
    }

    // Application Ready Event Listener
    @Bean
    public ApplicationListener<ApplicationReadyEvent> onApplicationReadyEventListener() {
        return event -> System.out.println("Application started successfully with profiles: " 
                                            + String.join(", ", event.getApplicationContext().getEnvironment().getActiveProfiles()));
    }

    // Custom Banner Implementation
    static class CustomBanner implements Banner {
        @Override
        public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
            out.println("===================================");
            out.println("   WELCOME TO THE EMPLOYEE MANAGEMENT SYSTEM  ");
            out.println("===================================");
        }
    }
}
