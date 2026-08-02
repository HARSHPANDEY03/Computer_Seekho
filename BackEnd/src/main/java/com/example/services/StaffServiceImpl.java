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

    // =====================================================
    // CREATE STAFF
    // =====================================================

    @Override
    public StaffResponse createStaff(StaffRequest request) {

        // Check duplicate username
        if (staffRepository.existsByStaffUsername(
                request.getStaffUsername())) {

            throw new RuntimeException(
                    "Staff username already exists.");
        }

        // Check duplicate email
        if (staffRepository.existsByStaffEmail(
                request.getStaffEmail())) {

            throw new RuntimeException(
                    "Staff email already exists.");
        }

        // Find Role
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

        staff.setStaffUsername(request.getStaffUsername());

        // Encode password
        staff.setStaffPassword(
                passwordEncoder.encode(
                        request.getStaffPassword()));

        /*
         * Get the role name from UserRole.
         *
         * Example:
         *
         * userRoleId = 1
         * roleName = ADMIN
         *
         * This prevents conflicting role information.
         */
        staff.setStaffRole(userRole.getRoleName());

        staff.setUserRole(userRole);

        Staff savedStaff =
                staffRepository.save(staff);

        return convertToResponse(savedStaff);
    }

    // =====================================================
    // GET ALL STAFF
    // =====================================================

    @Override
    public List<StaffResponse> getAllStaff() {

        return staffRepository
                .findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =====================================================
    // GET STAFF BY ID
    // =====================================================

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

    // =====================================================
    // UPDATE STAFF
    // =====================================================

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

        // Check username duplication
        if (!staff.getStaffUsername()
                .equals(request.getStaffUsername())
                &&
                staffRepository.existsByStaffUsername(
                        request.getStaffUsername())) {

            throw new RuntimeException(
                    "Staff username already exists.");
        }

        // Check email duplication
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

        staff.setStaffUsername(
                request.getStaffUsername());

        staff.setStaffRole(
                userRole.getRoleName());

        staff.setUserRole(userRole);

        /*
         * Password is NOT updated here.
         *
         * Password changes are handled by:
         *
         * AuthService.changePassword()
         */

        Staff updatedStaff =
                staffRepository.save(staff);

        return convertToResponse(updatedStaff);
    }

    // =====================================================
    // DELETE STAFF
    // =====================================================

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

    // =====================================================
    // SEARCH STAFF
    // =====================================================

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

    // =====================================================
    // ENTITY -> RESPONSE DTO
    // =====================================================

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