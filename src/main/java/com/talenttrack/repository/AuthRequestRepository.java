package com.talenttrack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talenttrack.entity.AuthRequest;

@Repository
public interface AuthRequestRepository extends JpaRepository<AuthRequest, Long> {

    /**
     * Finds an AuthRequest by the specified username.
     *
     * @param username the username to search for.
     * @return an Optional containing the found AuthRequest or empty if not found.
     */
    Optional<AuthRequest> findByUsername(String username);

    /**
     * Finds an AuthRequest by the specified token.
     *
     * @param token the token to search for.
     * @return an Optional containing the found AuthRequest or empty if not found.
     */
    Optional<AuthRequest> findByToken(String token);
}
