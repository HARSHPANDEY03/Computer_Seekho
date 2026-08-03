// UserRoleController.java
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

    @PostMapping
    public ResponseEntity<UserRole> createRole(
            @RequestBody UserRole userRole) {

        UserRole createdRole =
                userRoleService.createRole(userRole);

        return new ResponseEntity<>(
                createdRole,
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserRole>> getAllRoles() {

        return ResponseEntity.ok(
                userRoleService.getAllRoles());
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<UserRole> getRoleById(
            @PathVariable("roleId") Integer roleId) {

        return ResponseEntity.ok(
                userRoleService.getRoleById(roleId));
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<UserRole> updateRole(
            @PathVariable("roleId") Integer roleId,
            @RequestBody UserRole userRole) {

        return ResponseEntity.ok(
                userRoleService.updateRole(roleId, userRole));
    }

    @PatchMapping("/{roleId}/activate")
    public ResponseEntity<UserRole> activateRole(
            @PathVariable("roleId") Integer roleId) {

        return ResponseEntity.ok(
                userRoleService.activateRole(roleId));
    }

    @PatchMapping("/{roleId}/deactivate")
    public ResponseEntity<UserRole> deactivateRole(
            @PathVariable("roleId") Integer roleId) {

        return ResponseEntity.ok(
                userRoleService.deactivateRole(roleId));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(
            @PathVariable("roleId") Integer roleId) {

        userRoleService.deleteRole(roleId);

        return ResponseEntity.noContent().build();
    }
}