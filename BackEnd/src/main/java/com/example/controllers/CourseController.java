package com.example.controllers;

import com.example.dto.CourseRequest;
import com.example.dto.CourseResponse;
import com.example.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CourseRequest courseRequest) {
        return new ResponseEntity<>(courseService.createCourse(courseRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Integer id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable Integer id, @RequestBody CourseRequest courseRequest) {
        return ResponseEntity.ok(courseService.updateCourse(id, courseRequest));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CourseResponse> updateCourseStatus(@PathVariable Integer id, @RequestParam Boolean active) {
        return ResponseEntity.ok(courseService.updateCourseStatus(id, active));
    }

    @GetMapping("/active")
    public ResponseEntity<List<CourseResponse>> getActiveCourses() {
        return ResponseEntity.ok(courseService.getActiveCourses());
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseResponse>> searchCourses(@RequestParam String keyword) {
        return ResponseEntity.ok(courseService.searchCourses(keyword));
    }
}