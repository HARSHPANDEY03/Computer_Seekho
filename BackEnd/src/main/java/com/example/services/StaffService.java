package com.example.services;

import java.util.List;

import com.example.dto.StaffRequest;
import com.example.dto.StaffResponse;

public interface StaffService {

    // Create Staff
    StaffResponse createStaff(StaffRequest request);

    // Get All Staff
    List<StaffResponse> getAllStaff();

    // Get Staff By ID
    StaffResponse getStaffById(Integer staffId);

    // Update Staff
    StaffResponse updateStaff(
            Integer staffId,
            StaffRequest request);

    // Delete Staff
    void deleteStaff(Integer staffId);

    // Search Staff By Name
    List<StaffResponse> searchStaffByName(String staffName);
}