package com.example.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dto.RoleRequest;
import com.example.dto.RoleResponse;
import com.example.services.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Get all roles
    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    // Get role by ID
    @GetMapping("/{roleId}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Integer roleId) {
        return ResponseEntity.ok(roleService.getRoleById(roleId));
    }

    // Create new role
    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@RequestBody RoleRequest roleRequest) {

        RoleResponse response = roleService.createRole(roleRequest);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Update role
    @PutMapping("/{roleId}")
    public ResponseEntity<RoleResponse> updateRole(
            @PathVariable Integer roleId,
            @RequestBody RoleRequest roleRequest) {

        return ResponseEntity.ok(
                roleService.updateRole(roleId, roleRequest));
    }

    // Delete role
    @DeleteMapping("/{roleId}")
    public ResponseEntity<String> deleteRole(@PathVariable Integer roleId) {

        roleService.deleteRole(roleId);

        return ResponseEntity.ok("Role deleted successfully.");
    }

    // Assign role to staff
    @PatchMapping("/{roleId}/assign/{staffId}")
    public ResponseEntity<String> assignRoleToStaff(
            @PathVariable Integer roleId,
            @PathVariable Integer staffId) {

        roleService.assignRoleToStaff(roleId, staffId);

        return ResponseEntity.ok("Role assigned successfully.");
    }

    // Remove role from staff
    @PatchMapping("/{roleId}/remove/{staffId}")
    public ResponseEntity<String> removeRoleFromStaff(
            @PathVariable Integer roleId,
            @PathVariable Integer staffId) {

        roleService.removeRoleFromStaff(roleId, staffId);

        return ResponseEntity.ok("Role removed successfully.");
    }

}