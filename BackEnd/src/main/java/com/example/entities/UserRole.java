package com.example.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "user_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role_name", nullable = false, unique = true, length = 50)
    private String roleName;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "userRole",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Staff> staffList = new ArrayList<>();

    // Default Constructor
    public UserRole() {
    }

    // Parameterized Constructor
    public UserRole(String roleName, String description, Boolean isActive) {
        this.roleName = roleName;
        this.description = description;
        this.isActive = isActive;
    }

    // ==========================
    // Getters and Setters
    // ==========================

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }

    // ==========================
    // Utility Methods
    // ==========================

    public void addStaff(Staff staff) {
        staffList.add(staff);
        staff.setUserRole(this);
    }

    public void removeStaff(Staff staff) {
        staffList.remove(staff);
        staff.setUserRole(null);
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "userId=" + userId +
                ", roleName='" + roleName + '\'' +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}