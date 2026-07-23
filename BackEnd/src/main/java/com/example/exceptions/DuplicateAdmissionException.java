package com.example.exceptions;

public class DuplicateAdmissionException extends RuntimeException {

    public DuplicateAdmissionException(String message) {
        super(message);
    }
}