// services/StaffServiceImpl.java
package com.example.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.StaffRequest;
import com.example.dto.StaffResponse;
import com.example.entities.Staff;
import com.example.entities.UserRole;
import com.example.repositories.StaffRepository;
import com.example.repositories.UserRoleRepository;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public StaffServiceImpl(
            StaffRepository staffRepository,
            UserRoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder) {

        this.staffRepository = staffRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public StaffResponse createStaff(StaffRequest request) {

        if (staffRepository.existsByStaffUsername(
                request.getStaffUsername())) {

            throw new RuntimeException(
                    "Staff username already exists.");
        }

        if (staffRepository.existsByStaffEmail(
                request.getStaffEmail())) {

            throw new RuntimeException(
                    "Staff email already exists.");
        }

        UserRole userRole = userRoleRepository
                .findById(request.getUserRoleId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User role not found with ID: "
                                        + request.getUserRoleId()));

        Staff staff = new Staff();

        staff.setStaffName(request.getStaffName());

        staff.setPhotoUrl(request.getPhotoUrl());

        staff.setStaffMobile(request.getStaffMobile());

        staff.setStaffEmail(request.getStaffEmail());

        staff.setDescription(request.getDescription());

        staff.setStaffUsername(request.getStaffUsername());

        staff.setStaffPassword(
                passwordEncoder.encode(
                        request.getStaffPassword()));

        staff.setStaffRole(userRole.getRoleName());

        staff.setUserRole(userRole);

        Staff savedStaff =
                staffRepository.save(staff);

        return convertToResponse(savedStaff);
    }

    @Override
    public List<StaffResponse> getAllStaff() {

        return staffRepository
                .findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public StaffResponse getStaffById(Integer staffId) {

        Staff staff = staffRepository
                .findById(staffId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Staff not found with ID: "
                                        + staffId));

        return convertToResponse(staff);
    }

    @Override
    public StaffResponse updateStaff(
            Integer staffId,
            StaffRequest request) {

        Staff staff = staffRepository
                .findById(staffId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Staff not found with ID: "
                                        + staffId));

        if (!staff.getStaffUsername()
                .equals(request.getStaffUsername())
                &&
                staffRepository.existsByStaffUsername(
                        request.getStaffUsername())) {

            throw new RuntimeException(
                    "Staff username already exists.");
        }

        if (!staff.getStaffEmail()
                .equals(request.getStaffEmail())
                &&
                staffRepository.existsByStaffEmail(
                        request.getStaffEmail())) {

            throw new RuntimeException(
                    "Staff email already exists.");
        }

        UserRole userRole = userRoleRepository
                .findById(request.getUserRoleId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User role not found with ID: "
                                        + request.getUserRoleId()));

        staff.setStaffName(
                request.getStaffName());

        staff.setPhotoUrl(
                request.getPhotoUrl());

        staff.setStaffMobile(
                request.getStaffMobile());

        staff.setStaffEmail(
                request.getStaffEmail());

        staff.setDescription(
                request.getDescription());

        staff.setStaffUsername(
                request.getStaffUsername());

        staff.setStaffRole(
                userRole.getRoleName());

        staff.setUserRole(userRole);

        Staff updatedStaff =
                staffRepository.save(staff);

        return convertToResponse(updatedStaff);
    }

    @Override
    public void deleteStaff(Integer staffId) {

        Staff staff = staffRepository
                .findById(staffId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Staff not found with ID: "
                                        + staffId));

        staffRepository.delete(staff);
    }

    @Override
    public List<StaffResponse> searchStaffByName(
            String staffName) {

        return staffRepository
                .findByStaffNameContainingIgnoreCase(
                        staffName)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private StaffResponse convertToResponse(
            Staff staff) {

        StaffResponse response =
                new StaffResponse();

        response.setStaffId(
                staff.getStaffId());

        response.setStaffName(
                staff.getStaffName());

        response.setPhotoUrl(
                staff.getPhotoUrl());

        response.setStaffMobile(
                staff.getStaffMobile());

        response.setStaffEmail(
                staff.getStaffEmail());

        response.setDescription(
                staff.getDescription());

        response.setStaffUsername(
                staff.getStaffUsername());

        response.setStaffRole(
                staff.getStaffRole());

        if (staff.getUserRole() != null) {

            response.setUserRoleId(
                    staff.getUserRole().getUserId());

            response.setRoleName(
                    staff.getUserRole().getRoleName());
        }

        return response;
    }
}