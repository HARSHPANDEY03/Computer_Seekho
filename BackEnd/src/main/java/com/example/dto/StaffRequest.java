package com.example.dto;

public class StaffRequest {

    private String staffName;
    private String photoUrl;
    private Long staffMobile;
    private String staffEmail;
    private String staffUsername;
    private String staffPassword;
    private String staffRole;
    private Integer userRoleId;

    // Default Constructor
    public StaffRequest() {
    }

    // Parameterized Constructor
    public StaffRequest(String staffName, String photoUrl, Long staffMobile,
                        String staffEmail, String staffUsername,
                        String staffPassword, String staffRole,
                        Integer userRoleId) {
        this.staffName = staffName;
        this.photoUrl = photoUrl;
        this.staffMobile = staffMobile;
        this.staffEmail = staffEmail;
        this.staffUsername = staffUsername;
        this.staffPassword = staffPassword;
        this.staffRole = staffRole;
        this.userRoleId = userRoleId;
    }

    // Getters and Setters

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Long getStaffMobile() {
        return staffMobile;
    }

    public void setStaffMobile(Long staffMobile) {
        this.staffMobile = staffMobile;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }

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

    public String getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(String staffRole) {
        this.staffRole = staffRole;
    }

    public Integer getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Integer userRoleId) {
        this.userRoleId = userRoleId;
    }

    @Override
    public String toString() {
        return "StaffRequest{" +
                "staffName='" + staffName + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", staffMobile=" + staffMobile +
                ", staffEmail='" + staffEmail + '\'' +
                ", staffUsername='" + staffUsername + '\'' +
                ", staffRole='" + staffRole + '\'' +
                ", userRoleId=" + userRoleId +
                '}';
    }
}