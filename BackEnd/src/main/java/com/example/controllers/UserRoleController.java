package com.example.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entities.UserRole;
import com.example.services.UserRoleService;

@RestController
@RequestMapping("/api/roles")
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    // Create Role
    @PostMapping
    public ResponseEntity<UserRole> createRole(
            @RequestBody UserRole userRole) {

        UserRole createdRole =
                userRoleService.createRole(userRole);

        return new ResponseEntity<>(
                createdRole,
                HttpStatus.CREATED);
    }

    // Get All Roles
    @GetMapping
    public ResponseEntity<List<UserRole>> getAllRoles() {

        return ResponseEntity.ok(
                userRoleService.getAllRoles());
    }

    // Get Role By ID
    @GetMapping("/{roleId}")
    public ResponseEntity<UserRole> getRoleById(
            @PathVariable Integer roleId) {

        return ResponseEntity.ok(
                userRoleService.getRoleById(roleId));
    }

    // Update Role
    @PutMapping("/{roleId}")
    public ResponseEntity<UserRole> updateRole(
            @PathVariable Integer roleId,
            @RequestBody UserRole userRole) {

        return ResponseEntity.ok(
                userRoleService.updateRole(roleId, userRole));
    }

    // Activate Role
    @PatchMapping("/{roleId}/activate")
    public ResponseEntity<UserRole> activateRole(
            @PathVariable Integer roleId) {

        return ResponseEntity.ok(
                userRoleService.activateRole(roleId));
    }

    // Deactivate Role
    @PatchMapping("/{roleId}/deactivate")
    public ResponseEntity<UserRole> deactivateRole(
            @PathVariable Integer roleId) {

        return ResponseEntity.ok(
                userRoleService.deactivateRole(roleId));
    }

    // Delete Role
    @DeleteMapping("/{roleId}")
    public ResponseEntity<String> deleteRole(
            @PathVariable Integer roleId) {

        userRoleService.deleteRole(roleId);

        return ResponseEntity.ok(
                "Role deleted successfully.");
    }
}