// StaffController.java
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

    @PostMapping
    public ResponseEntity<StaffResponse> createStaff(
            @RequestBody StaffRequest staffRequest) {

        StaffResponse response =
                staffService.createStaff(staffRequest);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StaffResponse>> getAllStaff() {

        return ResponseEntity.ok(
                staffService.getAllStaff());
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<StaffResponse> getStaffById(
            @PathVariable("staffId") Integer staffId) {

        return ResponseEntity.ok(
                staffService.getStaffById(staffId));
    }

    @PutMapping("/{staffId}")
    public ResponseEntity<StaffResponse> updateStaff(
            @PathVariable("staffId") Integer staffId,
            @RequestBody StaffRequest staffRequest) {

        return ResponseEntity.ok(
                staffService.updateStaff(
                        staffId,
                        staffRequest));
    }

    @DeleteMapping("/{staffId}")
    public ResponseEntity<Void> deleteStaff(
            @PathVariable("staffId") Integer staffId) {

        staffService.deleteStaff(staffId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<StaffResponse>> searchStaff(
            @RequestParam("keyword") String keyword) {

        return ResponseEntity.ok(
                staffService.searchStaffByName(keyword));
    }
}