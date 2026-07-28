package com.example.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.Student;

@Repository
public interface StudentRepository
        extends JpaRepository<Student, Integer> {

    Optional<Student> findByEnquiryEnquiryId(
            Integer enquiryId);

    boolean existsByEnquiryEnquiryId(
            Integer enquiryId);

    boolean existsByStudentUsername(
            String studentUsername);

    List<Student> findByStudentNameContainingIgnoreCase(
            String studentName);

    List<Student> findByStudentMobile(
            Long studentMobile);
}