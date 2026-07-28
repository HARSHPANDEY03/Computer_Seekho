package com.example.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.Staff;

@Repository
public interface StaffRepository
        extends JpaRepository<Staff, Integer> {

    // Used for Login
    Optional<Staff> findByStaffUsername(String staffUsername);

    // Find by email
    Optional<Staff> findByStaffEmail(String staffEmail);

    // Duplicate username validation
    boolean existsByStaffUsername(String staffUsername);

    // Duplicate email validation
    boolean existsByStaffEmail(String staffEmail);

    // Search staff
    List<Staff> findByStaffNameContainingIgnoreCase(String staffName);

    // Find staff by role
    List<Staff> findByStaffRole(String staffRole);
}