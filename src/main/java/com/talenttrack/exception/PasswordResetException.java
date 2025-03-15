package com.talenttrack.exception;

public class PasswordResetException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Constructor with a message
    public PasswordResetException(String message) {
        super(message);
    }

    // Constructor with a message and a cause
    public PasswordResetException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with only the cause
    public PasswordResetException(Throwable cause) {
        super(cause);
    }
}
