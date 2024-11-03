package com.spring.project.DataValidation.CrudApplication.Exception;

@SuppressWarnings("serial")
public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
