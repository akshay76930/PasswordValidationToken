package com.spring.project.DataValidation.CrudApplication.Service;

import com.spring.project.DataValidation.CrudApplication.Entity.AuthRequest;
import com.spring.project.DataValidation.CrudApplication.Repository.AuthRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthRequestService {

	@Autowired
	private AuthRequestRepository authRequestRepository;

	public AuthRequest saveAuthRequest(AuthRequest authRequest) {
		return authRequestRepository.save(authRequest);
	}
}
