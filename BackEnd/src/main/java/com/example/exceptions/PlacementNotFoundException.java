package com.example.exceptions;

public class PlacementNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PlacementNotFoundException() {
        super();
    }

    public PlacementNotFoundException(String message) {
        super(message);
    }

    public PlacementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}