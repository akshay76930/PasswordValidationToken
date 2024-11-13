package com.spring.project.DataValidation.CrudApplication.Service;

import com.spring.project.DataValidation.CrudApplication.Entity.AuthRequest;
import com.spring.project.DataValidation.CrudApplication.Repository.AuthRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthRequestService {

    // Logger instance to log relevant information
    private static final Logger logger = LoggerFactory.getLogger(AuthRequestService.class);

    // Repository instance to handle AuthRequest entity database operations
    private final AuthRequestRepository authRequestRepository;

    // Constructor-based injection of AuthRequestRepository to follow best practices
    @Autowired
    public AuthRequestService(AuthRequestRepository authRequestRepository) {
        this.authRequestRepository = authRequestRepository;
    }

    /**
     * Method to save an AuthRequest entity to the database.
     *
     * @param authRequest The AuthRequest object to be saved.
     * @return The saved AuthRequest entity with generated ID.
     */
    public AuthRequest saveAuthRequest(AuthRequest authRequest) {
        // Save the AuthRequest and capture the saved entity with ID
        AuthRequest savedAuthRequest = authRequestRepository.save(authRequest);
        
        // Log the information about the saved AuthRequest entity
        logger.info("AuthRequest saved with ID: {}", savedAuthRequest.getId());
        
        // Return the saved entity
        return savedAuthRequest;
    }
}
