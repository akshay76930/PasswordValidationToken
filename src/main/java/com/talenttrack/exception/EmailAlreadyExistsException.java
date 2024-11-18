package com.talenttrack.exception;

/**
 * Custom exception for scenarios where the email already exists in the system.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new EmailAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message
     */
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
