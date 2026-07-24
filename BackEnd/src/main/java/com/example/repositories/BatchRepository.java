package com.example.repositories;

import com.example.entities.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Integer> {
    List<Batch> findByCourseCourseId(Integer courseId);
    List<Batch> findByCourseCourseIdAndBatchIsActiveTrue(Integer courseId);
}