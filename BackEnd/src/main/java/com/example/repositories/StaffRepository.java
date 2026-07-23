package com.example.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.Staff;
import com.example.entities.UserRole;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {


    Optional<Staff> findByStaffUsername(String staffUsername);

    Optional<Staff> findByStaffEmail(String staffEmail);

    

    boolean existsByStaffUsername(String staffUsername);

    boolean existsByStaffEmail(String staffEmail);


    List<Staff> findByStaffNameContainingIgnoreCase(String staffName);

    List<Staff> findByStaffRole(String staffRole);

    List<Staff> findByUserRole(UserRole userRole);

}