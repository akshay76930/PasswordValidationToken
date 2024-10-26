package com.spring.project.DataValidation.CrudApplication.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.project.DataValidation.CrudApplication.Entity.AuthRequest;

@Repository
public interface AuthRequestRepository extends JpaRepository<AuthRequest, Long> {

}

