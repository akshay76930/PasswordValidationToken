package com.talenttrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for managing validation errors and custom exceptions.
 */
@ControllerAdvice
public class ExceptionPasswordValidation {

    /**
     * Handles validation errors for method arguments.
     *
     * @param ex MethodArgumentNotValidException containing validation details
     * @return ResponseEntity with field error details and a 400 Bad Request status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                                       .getFieldErrors()
                                       .stream()
                                       .collect(Collectors.toMap(
                                           error -> error.getField(), 
                                           error -> error.getDefaultMessage()
                                       ));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Handles InvalidPasswordException for invalid password scenarios.
     *
     * @param ex InvalidPasswordException containing error details
     * @return ResponseEntity with error message and a 401 Unauthorized status
     */
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPasswordException(InvalidPasswordException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
