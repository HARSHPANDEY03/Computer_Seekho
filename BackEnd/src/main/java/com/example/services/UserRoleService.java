package com.example.services;

import java.util.List;

import com.example.entities.UserRole;

public interface UserRoleService {

    // Create a new role
    UserRole createRole(UserRole userRole);

    // Get all roles
    List<UserRole> getAllRoles();

    // Get role by ID
    UserRole getRoleById(Integer roleId);

    // Update existing role
    UserRole updateRole(Integer roleId, UserRole userRole);

    // Activate a role
    UserRole activateRole(Integer roleId);

    // Deactivate a role
    UserRole deactivateRole(Integer roleId);

    // Delete a role
    void deleteRole(Integer roleId);
}