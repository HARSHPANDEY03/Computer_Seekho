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

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    // Get all staff
    @GetMapping
    public ResponseEntity<List<StaffResponse>> getAllStaff() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    // Get staff by ID
    @GetMapping("/{staffId}")
    public ResponseEntity<StaffResponse> getStaffById(@PathVariable Integer staffId) {
        return ResponseEntity.ok(staffService.getStaffById(staffId));
    }

    // Add new staff
    @PostMapping
    public ResponseEntity<StaffResponse> addStaff(@RequestBody StaffRequest staffRequest) {
        StaffResponse response = staffService.addStaff(staffRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Update staff
    @PutMapping("/{staffId}")
    public ResponseEntity<StaffResponse> updateStaff(
            @PathVariable Integer staffId,
            @RequestBody StaffRequest staffRequest) {

        return ResponseEntity.ok(
                staffService.updateStaff(staffId, staffRequest));
    }

    // Delete staff
    @DeleteMapping("/{staffId}")
    public ResponseEntity<String> deleteStaff(@PathVariable Integer staffId) {

        staffService.deleteStaff(staffId);

        return ResponseEntity.ok("Staff deleted successfully.");
    }

    // Activate staff
    @PatchMapping("/{staffId}/activate")
    public ResponseEntity<String> activateStaff(@PathVariable Integer staffId) {

        staffService.activateStaff(staffId);

        return ResponseEntity.ok("Staff activated successfully.");
    }

    // Deactivate staff
    @PatchMapping("/{staffId}/deactivate")
    public ResponseEntity<String> deactivateStaff(@PathVariable Integer staffId) {

        staffService.deactivateStaff(staffId);

        return ResponseEntity.ok("Staff deactivated successfully.");
    }

    // Search staff
    @GetMapping("/search")
    public ResponseEntity<List<StaffResponse>> searchStaff(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                staffService.searchStaff(keyword));
    }

}