package com.talenttrack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talenttrack.entity.AuthRequest;

@Repository
public interface AuthRequestRepository extends JpaRepository<AuthRequest, Long> {

	Optional<AuthRequest> findByUsername(String username);

	Optional<AuthRequest> findByToken(String token);
}
