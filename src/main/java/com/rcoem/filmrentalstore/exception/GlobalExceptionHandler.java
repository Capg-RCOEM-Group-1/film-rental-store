package com.rcoem.filmrentalstore.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //for @NotNull, @Email, etc, from the database layer
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        //looping through each violation
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }

        log.warn("Constraint Violations: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    //if passing "abc" into a Long ID URL path
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<String> handleDataRestTypeMismatch(Exception ex) {
        log.warn("Bad Endpoint: {}", ex.getMessage());
        return new ResponseEntity<>("Invalid parameter type provided", HttpStatus.BAD_REQUEST);
    }

    //handles all unique constraint violations and database integrity errors
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(org.springframework.dao.DataIntegrityViolationException ex) {
        String rootMsg = ex.getMostSpecificCause().getMessage();
        log.warn("Integrity Violation: {}", rootMsg);

        //checking if it's specifically a uniqueness constraint violation
        if (rootMsg != null && rootMsg.contains("Duplicate entry")) {
            return new ResponseEntity<>("A record with this value already exists. Please provide a unique value.", HttpStatus.CONFLICT);
        }

        //else fallback for other database integrity issues like foreign key failures, null constraints
        return new ResponseEntity<>("A database constraint was violated. Please check your input data.", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex){
        log.warn("Resource Not Found: {}", ex.getMessage());
        return new ResponseEntity<>("Resource not found with given identifier",HttpStatus.NOT_FOUND);
    }
}