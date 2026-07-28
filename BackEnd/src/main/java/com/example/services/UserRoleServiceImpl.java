package com.example.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entities.UserRole;
import com.example.repositories.UserRoleRepository;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    // Constructor Injection
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    // =========================================================
    // CREATE ROLE
    // =========================================================

    @Override
    public UserRole createRole(UserRole userRole) {

        // Check duplicate role name
        if (userRoleRepository.existsByRoleName(userRole.getRoleName())) {
            throw new RuntimeException(
                    "Role already exists: " + userRole.getRoleName());
        }

        // New role will be active by default
        if (userRole.getIsActive() == null) {
            userRole.setIsActive(true);
        }

        return userRoleRepository.save(userRole);
    }

    // =========================================================
    // GET ALL ROLES
    // =========================================================

    @Override
    public List<UserRole> getAllRoles() {
        return userRoleRepository.findAll();
    }

    // =========================================================
    // GET ROLE BY ID
    // =========================================================

    @Override
    public UserRole getRoleById(Integer roleId) {

        return userRoleRepository
                .findById(roleId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Role not found with ID: " + roleId));
    }

    // =========================================================
    // UPDATE ROLE
    // =========================================================

    @Override
    public UserRole updateRole(Integer roleId, UserRole userRole) {

        UserRole existingRole = userRoleRepository
                .findById(roleId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Role not found with ID: " + roleId));

        /*
         * Check duplicate role name only when the role name
         * is actually being changed.
         */
        if (!existingRole.getRoleName().equals(userRole.getRoleName())
                && userRoleRepository.existsByRoleName(userRole.getRoleName())) {

            throw new RuntimeException(
                    "Role already exists: " + userRole.getRoleName());
        }

        existingRole.setRoleName(userRole.getRoleName());
        existingRole.setDescription(userRole.getDescription());

        /*
         * We don't update isActive here.
         *
         * Activation/deactivation is handled separately
         * through activateRole() and deactivateRole().
         */

        return userRoleRepository.save(existingRole);
    }

    // =========================================================
    // ACTIVATE ROLE
    // =========================================================

    @Override
    public UserRole activateRole(Integer roleId) {

        UserRole userRole = userRoleRepository
                .findById(roleId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Role not found with ID: " + roleId));

        userRole.setIsActive(true);

        return userRoleRepository.save(userRole);
    }

    // =========================================================
    // DEACTIVATE ROLE
    // =========================================================

    @Override
    public UserRole deactivateRole(Integer roleId) {

        UserRole userRole = userRoleRepository
                .findById(roleId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Role not found with ID: " + roleId));

        userRole.setIsActive(false);

        return userRoleRepository.save(userRole);
    }

    // =========================================================
    // DELETE ROLE
    // =========================================================

    @Override
    public void deleteRole(Integer roleId) {

        UserRole userRole = userRoleRepository
                .findById(roleId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Role not found with ID: " + roleId));

        /*
         * Don't delete a role if staff members are
         * currently assigned to it.
         */
        if (userRole.getStaffList() != null
                && !userRole.getStaffList().isEmpty()) {

            throw new RuntimeException(
                    "Cannot delete role because staff members are assigned to it.");
        }

        userRoleRepository.delete(userRole);
    }
}