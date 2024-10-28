package com.spring.project.DataValidation.CrudApplication.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for handling validation errors across the application.
 */
@ControllerAdvice
public class ExceptionPasswordValidation {

    /**
     * Handles MethodArgumentNotValidException thrown when a method argument fails validation.
     *
     * @param ex the MethodArgumentNotValidException instance containing details of the validation error
     * @return a ResponseEntity containing a map of field names and their respective error messages,
     *         along with a 400 Bad Request status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Collecting field errors into a map where the key is the field name and the value is the error message
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),        
                        error -> error.getDefaultMessage()
                ));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
