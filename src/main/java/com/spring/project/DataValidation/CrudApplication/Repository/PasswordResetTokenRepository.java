package com.spring.project.DataValidation.CrudApplication.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.project.DataValidation.CrudApplication.Entity.PasswordResetToken;
import com.spring.project.DataValidation.CrudApplication.Entity.User;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    /**
     * Finds a PasswordResetToken by its token string.
     *
     * @param token the token string to search for.
     * @return an Optional containing the found PasswordResetToken or empty if not found.
     */
    Optional<PasswordResetToken> findByToken(String token);

    /**
     * Finds all PasswordResetTokens associated with a specific user.
     *
     * @param user the user whose tokens are to be found.
     * @return a list of PasswordResetTokens associated with the user.
     */
    List<PasswordResetToken> findByUser(User user);

    /**
     * Finds all PasswordResetTokens that have expired before the specified date.
     *
     * @param expirationDate the date to check for expired tokens.
     * @return a list of expired PasswordResetTokens.
     */
    List<PasswordResetToken> findByExpirationDateBefore(LocalDateTime expirationDate);

    /**
     * Deletes a PasswordResetToken by its token string.
     *
     * @param token the token string of the token to be deleted.
     * @return the number of tokens deleted (0 or 1).
     */
    long deleteByToken(String token);
    
    /**
     * Checks if a PasswordResetToken exists for the given user.
     *
     * @param user the user whose token is being checked.
     * @return true if a token exists for the user, false otherwise.
     */
    boolean existsByUser(User user);
}
