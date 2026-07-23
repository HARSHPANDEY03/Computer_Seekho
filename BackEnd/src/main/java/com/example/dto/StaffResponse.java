package com.example.dto;

public class StaffResponse {

    private Integer staffId;
    private String staffName;
    private String photoUrl;
    private Long staffMobile;
    private String staffEmail;
    private String staffUsername;
    private String staffRole;
    private Integer userRoleId;
    private String roleName;

    // Default Constructor
    public StaffResponse() {
    }

    // Parameterized Constructor
    public StaffResponse(Integer staffId,
                         String staffName,
                         String photoUrl,
                         Long staffMobile,
                         String staffEmail,
                         String staffUsername,
                         String staffRole,
                         Integer userRoleId,
                         String roleName) {
        this.staffId = staffId;
        this.staffName = staffName;
        this.photoUrl = photoUrl;
        this.staffMobile = staffMobile;
        this.staffEmail = staffEmail;
        this.staffUsername = staffUsername;
        this.staffRole = staffRole;
        this.userRoleId = userRoleId;
        this.roleName = roleName;
    }

    // Getters and Setters

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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "StaffResponse{" +
                "staffId=" + staffId +
                ", staffName='" + staffName + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", staffMobile=" + staffMobile +
                ", staffEmail='" + staffEmail + '\'' +
                ", staffUsername='" + staffUsername + '\'' +
                ", staffRole='" + staffRole + '\'' +
                ", userRoleId=" + userRoleId +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}