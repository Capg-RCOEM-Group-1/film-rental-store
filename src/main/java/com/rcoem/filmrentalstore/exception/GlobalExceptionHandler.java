package com.rcoem.filmrentalstore.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //for @NotNull, @Email, etc, from the database layer
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    //if passing "abc" into a Long ID URL path
    @ExceptionHandler({IllegalArgumentException.class, TypeMismatchException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<String> handleDataRestTypeMismatch(Exception ex) {
        return new ResponseEntity<>("Invalid parameter type provided", HttpStatus.BAD_REQUEST);
    }

    // handles the unique constraint violation for the film-category relationship
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDuplicateEntry(Exception ex) {
        return new ResponseEntity<>("Duplicate film-category entry not allowed", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex){
        return new ResponseEntity<>("Resource not found with given identifier",HttpStatus.NOT_FOUND);
    }
}