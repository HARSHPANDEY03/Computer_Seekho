package com.example.exceptions;

public class ClosureReasonNotFoundException extends ResourceNotFoundException {
    public ClosureReasonNotFoundException(String message) {
        super(message);
    }

    public ClosureReasonNotFoundException(Integer id) {
        super("Closure reason not found with ID: " + id);
    }
}