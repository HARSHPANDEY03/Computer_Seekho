package com.example.services;

import com.example.dto.CourseRequest;
import com.example.dto.CourseResponse;

import java.util.List;

public interface CourseService {
    CourseResponse createCourse(CourseRequest courseRequest);
    List<CourseResponse> getAllCourses();
    CourseResponse getCourseById(Integer id);
    CourseResponse updateCourse(Integer id, CourseRequest courseRequest);
    CourseResponse updateCourseStatus(Integer id, Boolean status);
    List<CourseResponse> getActiveCourses();
    List<CourseResponse> searchCourses(String keyword);
}