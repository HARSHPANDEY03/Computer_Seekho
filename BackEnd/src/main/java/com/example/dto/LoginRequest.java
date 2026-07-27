package com.example.dto;

public class LoginRequest {

    private String staffUsername;
    private String staffPassword;

    // Default Constructor
    public LoginRequest() {
    }

    // Parameterized Constructor
    public LoginRequest(String staffUsername, String staffPassword) {
        this.staffUsername = staffUsername;
        this.staffPassword = staffPassword;
    }

    // Getters and Setters
    public String getStaffUsername() {
        return staffUsername;
    }

    public void setStaffUsername(String staffUsername) {
        this.staffUsername = staffUsername;
    }

    public String getStaffPassword() {
        return staffPassword;
    }

    public void setStaffPassword(String staffPassword) {
        this.staffPassword = staffPassword;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "staffUsername='" + staffUsername + '\'' +
                '}';
    }
}