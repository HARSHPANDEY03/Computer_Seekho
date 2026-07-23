package com.example.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    // Find role by role name
    Optional<UserRole> findByRoleName(String roleName);

    // Check if a role already exists
    boolean existsByRoleName(String roleName);

    // Get all active roles
    List<UserRole> findByIsActiveTrue();

    // Get all inactive roles
    List<UserRole> findByIsActiveFalse();

}