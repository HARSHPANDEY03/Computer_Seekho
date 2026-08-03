package com.example.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Wrong username / wrong password on login.
     * Without this handler, Spring Security's ExceptionTranslationFilter
     * catches the AuthenticationException thrown by AuthenticationManager
     * and returns a bare 403 Forbidden with no body - which is the
     * confusing "Forbidden" error seen on the staff login screen.
     */
    @ExceptionHandler({ BadCredentialsException.class, UsernameNotFoundException.class, AuthenticationException.class })
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(
            AuthenticationException exception) {

        Map<String, Object> response = new HashMap<>();

        response.put("success", false);
        response.put("message", "Invalid username or password.");

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    /**
     * Catch-all for other RuntimeExceptions thrown from services
     * (e.g. "Staff not found", "Current Password is incorrect."),
     * so the client gets a readable message instead of a generic 500.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException exception) {

        Map<String, Object> response = new HashMap<>();

        response.put("success", false);
        response.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleStudentNotFound(
            StudentNotFoundException exception) {

        Map<String, Object> response = new HashMap<>();

        response.put("success", false);
        response.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    /**
     * Course/batch/order/payment-type not found while processing a
     * Razorpay order or verification request.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(
            ResourceNotFoundException exception) {

        Map<String, Object> response = new HashMap<>();

        response.put("success", false);
        response.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    /**
     * The Razorpay signature could not be verified - the payment cannot be
     * trusted, so nothing was persisted.
     */
    @ExceptionHandler(PaymentVerificationException.class)
    public ResponseEntity<Map<String, Object>> handlePaymentVerification(
            PaymentVerificationException exception) {

        Map<String, Object> response = new HashMap<>();

        response.put("success", false);
        response.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(response);
    }

    @ExceptionHandler(DuplicateAdmissionException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateAdmission(
            DuplicateAdmissionException exception) {

        Map<String, Object> response = new HashMap<>();

        response.put("success", false);
        response.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(
                        error.getField(),
                        error.getDefaultMessage()));

        return ResponseEntity
                .badRequest()
                .body(errors);
    }
}