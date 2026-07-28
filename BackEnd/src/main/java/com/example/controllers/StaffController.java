package com.example.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dto.StaffRequest;
import com.example.dto.StaffResponse;
import com.example.services.StaffService;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(
            StaffService staffService) {

        this.staffService = staffService;
    }

    // =====================================================
    // CREATE STAFF
    // =====================================================

    @PostMapping
    public ResponseEntity<StaffResponse> createStaff(
            @RequestBody StaffRequest staffRequest) {

        StaffResponse response =
                staffService.createStaff(staffRequest);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED);
    }

    // =====================================================
    // GET ALL STAFF
    // =====================================================

    @GetMapping
    public ResponseEntity<List<StaffResponse>> getAllStaff() {

        return ResponseEntity.ok(
                staffService.getAllStaff());
    }

    // =====================================================
    // GET STAFF BY ID
    // =====================================================

    @GetMapping("/{staffId}")
    public ResponseEntity<StaffResponse> getStaffById(
            @PathVariable Integer staffId) {

        return ResponseEntity.ok(
                staffService.getStaffById(staffId));
    }

    // =====================================================
    // UPDATE STAFF
    // =====================================================

    @PutMapping("/{staffId}")
    public ResponseEntity<StaffResponse> updateStaff(
            @PathVariable Integer staffId,
            @RequestBody StaffRequest staffRequest) {

        return ResponseEntity.ok(
                staffService.updateStaff(
                        staffId,
                        staffRequest));
    }

    // =====================================================
    // DELETE STAFF
    // =====================================================

    @DeleteMapping("/{staffId}")
    public ResponseEntity<String> deleteStaff(
            @PathVariable Integer staffId) {

        staffService.deleteStaff(staffId);

        return ResponseEntity.ok(
                "Staff deleted successfully.");
    }

    // =====================================================
    // SEARCH STAFF
    // =====================================================

    @GetMapping("/search")
    public ResponseEntity<List<StaffResponse>> searchStaff(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                staffService.searchStaffByName(keyword));
    }
}