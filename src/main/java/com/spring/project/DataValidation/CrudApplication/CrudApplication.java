package com.spring.project.DataValidation.CrudApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class CrudApplication {

    private static final Logger logger = LoggerFactory.getLogger(CrudApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CrudApplication.class);

        // Customizing application properties programmatically (optional)
        app.setAdditionalProfiles("prod"); // Can be switched to 'dev' or 'test' for different environments
        app.run(args);
    }

    // Method to run after the application starts successfully
    @EventListener(ApplicationReadyEvent.class)
    public void applicationStarted() {
        logger.info("Application has started successfully!");
    }

    // Global Exception Handler for uncaught exceptions
    @Bean
    public Thread.UncaughtExceptionHandler globalExceptionHandler() {
        return (thread, exception) -> {
            logger.error("Uncaught exception in thread '{}': {}", thread.getName(), exception.getMessage(), exception);
        };
    }

    // Customize any other startup behavior or services
    @Bean
    public void additionalInitialization() {
        // Any additional beans or startup tasks can be placed here.
        logger.info("Additional initialization tasks executed during startup.");
    }
}
