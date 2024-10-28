package com.spring.project.DataValidation.CrudApplication.Service;

import com.spring.project.DataValidation.CrudApplication.Entity.AuthRequest;
import com.spring.project.DataValidation.CrudApplication.Repository.AuthRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthRequestService {

    private static final Logger logger = LoggerFactory.getLogger(AuthRequestService.class);
    private final AuthRequestRepository authRequestRepository;

    @Autowired
    public AuthRequestService(AuthRequestRepository authRequestRepository) {
        this.authRequestRepository = authRequestRepository;
    }

    public AuthRequest saveAuthRequest(AuthRequest authRequest) {
        AuthRequest savedAuthRequest = authRequestRepository.save(authRequest);
        logger.info("AuthRequest saved with ID: {}", savedAuthRequest.getId());
        return savedAuthRequest;
    }
}
