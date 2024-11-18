package com.talenttrack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talenttrack.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user.
     * @return an Optional containing the found User or empty if not found.
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user exists by their username.
     *
     * @param username the username to check for existence.
     * @return true if the user exists, false otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user exists by their email address.
     *
     * @param email the email address to check for existence.
     * @return true if the user exists, false otherwise.
     */
    boolean existsByEmail(String email);
    
    /**
     * Deletes a user by their username.
     *
     * @param username the username of the user to be deleted.
     */
    void deleteByUsername(String username);
}
