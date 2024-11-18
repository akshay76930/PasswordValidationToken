package com.talenttrack.exception;

/**
 * Custom exception for invalid password scenarios.
 */
public class InvalidPasswordException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new InvalidPasswordException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidPasswordException(String message) {
        super(message);
    }
}
