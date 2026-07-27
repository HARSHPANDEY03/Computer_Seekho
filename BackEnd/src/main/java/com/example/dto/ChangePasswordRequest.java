package com.example.dto;

public class ChangePasswordRequest {

    private String staffUsername;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    // Default Constructor
    public ChangePasswordRequest() {
    }

    // Parameterized Constructor
    public ChangePasswordRequest(String staffUsername,
                                 String currentPassword,
                                 String newPassword,
                                 String confirmPassword) {
        this.staffUsername = staffUsername;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    // Getters and Setters

    public String getStaffUsername() {
        return staffUsername;
    }

    public void setStaffUsername(String staffUsername) {
        this.staffUsername = staffUsername;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "ChangePasswordRequest{" +
                "staffUsername='" + staffUsername + '\'' +
                '}';
    }
}