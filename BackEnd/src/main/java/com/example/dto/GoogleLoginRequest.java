package com.example.dto;

public class GoogleLoginRequest {

    // The ID token returned by Google Identity Services in the browser.
    // NOT an access token - this is a signed JWT issued by Google itself,
    // which we verify server-side before trusting anything in it.
    private String idToken;

    // Default Constructor
    public GoogleLoginRequest() {
    }

    // Parameterized Constructor
    public GoogleLoginRequest(String idToken) {
        this.idToken = idToken;
    }

    // Getters and Setters
    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}