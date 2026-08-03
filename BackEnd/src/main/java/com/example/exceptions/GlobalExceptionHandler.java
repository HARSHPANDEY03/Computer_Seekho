package com.example.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
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
     * Any database-level rule violation: deleting a row something else
     * still references (foreign key), saving a value too long for its
     * column, a duplicate value on a unique column, etc. Spring wraps all
     * of these in DataIntegrityViolationException, and its default message
     * is the raw JDBC/SQL error - including the full failing statement,
     * which is meaningless (and slightly alarming) to an end user. This
     * handler is picked over the generic RuntimeException one below
     * automatically, since Spring always matches the most specific
     * exception type first - translate it into one clean, genuine sentence
     * instead of leaking that SQL text to the browser.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(
            DataIntegrityViolationException exception) {

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);

        Throwable mostSpecific = exception.getMostSpecificCause();
        String raw = mostSpecific != null ? mostSpecific.getMessage() : exception.getMessage();
        String lower = raw != null ? raw.toLowerCase() : "";

        String friendly;
        if (lower.contains("foreign key constraint fails") && lower.contains("delete")) {
            friendly = "This can't be deleted because other records still depend on it. "
                    + "Remove or reassign those related records first, then try again.";
        } else if (lower.contains("foreign key constraint fails")) {
            friendly = "This couldn't be saved because it references something that no longer exists. "
                    + "Please double-check the related selection and try again.";
        } else if (lower.contains("data too long") || lower.contains("data truncation")) {
            friendly = "One of the values entered is too long to be saved. Please shorten it and try again.";
        } else if (lower.contains("duplicate entry")) {
            friendly = "A record with this value already exists. Please use a different value.";
        } else {
            friendly = "This couldn't be saved due to a data conflict. Please check the values and try again.";
        }

        response.put("message", friendly);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
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