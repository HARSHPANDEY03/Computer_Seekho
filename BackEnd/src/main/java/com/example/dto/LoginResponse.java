package com.example.dto;

public class LoginResponse {

    private String accessToken;
    private String tokenType = "Bearer";

    private Integer staffId;
    private String staffName;
    private String staffUsername;
    private String staffEmail;
    private String staffRole;

    // Default Constructor
    public LoginResponse() {
    }

    // Parameterized Constructor
    public LoginResponse(String accessToken,
                         Integer staffId,
                         String staffName,
                         String staffUsername,
                         String staffEmail,
                         String staffRole) {
        this.accessToken = accessToken;
        this.staffId = staffId;
        this.staffName = staffName;
        this.staffUsername = staffUsername;
        this.staffEmail = staffEmail;
        this.staffRole = staffRole;
    }

    // Getters and Setters

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffUsername() {
        return staffUsername;
    }

    public void setStaffUsername(String staffUsername) {
        this.staffUsername = staffUsername;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }

    public String getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(String staffRole) {
        this.staffRole = staffRole;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "tokenType='" + tokenType + '\'' +
                ", staffId=" + staffId +
                ", staffName='" + staffName + '\'' +
                ", staffUsername='" + staffUsername + '\'' +
                ", staffEmail='" + staffEmail + '\'' +
                ", staffRole='" + staffRole + '\'' +
                '}';
    }
}