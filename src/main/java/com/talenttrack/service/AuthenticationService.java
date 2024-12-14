package com.talenttrack.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talenttrack.entity.AuthRequest;
import com.talenttrack.repository.AuthRequestRepository;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final AuthRequestRepository authRequestRepository;

    @Autowired
    public AuthenticationService(AuthRequestRepository authRequestRepository) {
        this.authRequestRepository = authRequestRepository;
    }

    public AuthRequest saveAuthRequest(AuthRequest authRequest) {
        AuthRequest savedAuthRequest = authRequestRepository.save(authRequest);
        logger.info("AuthRequest saved with ID: {}", savedAuthRequest.getId());
        return savedAuthRequest;
    }
}
