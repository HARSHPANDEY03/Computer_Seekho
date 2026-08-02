package com.example.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.PlacedStudent;

@Repository
public interface PlacedStudentRepository extends JpaRepository<PlacedStudent, Integer> {

    List<PlacedStudent> findByRecruiterRecruiterId(Integer recruiterId);

    List<PlacedStudent> findByBatchBatchId(Integer batchId);

}