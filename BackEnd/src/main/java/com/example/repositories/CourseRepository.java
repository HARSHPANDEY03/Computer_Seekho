package com.example.repositories;


import com.example.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByCourseIsActiveTrue();
    List<Course> findByCourseNameContainingIgnoreCaseOrCourseCategoryContainingIgnoreCase(String name, String category);
}