package com.example.exceptions;

public class RecruiterNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    

    public RecruiterNotFoundException() {
        super();
    }

    public RecruiterNotFoundException(String message) {
        super(message);
    }

    public RecruiterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}