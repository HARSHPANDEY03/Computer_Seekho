// StudentController.java
package com.example.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.StudentRequest;
import com.example.dto.StudentResponse;
import com.example.services.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/register")
    public ResponseEntity<StudentResponse> registerStudent(
            @Valid @RequestBody StudentRequest request) {

        StudentResponse response =
                studentService.registerStudent(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {

        List<StudentResponse> students =
                studentService.getAllStudents();

        return ResponseEntity.ok(students);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponse> getStudentById(
            @PathVariable("studentId") Integer studentId) {

        StudentResponse response =
                studentService.getStudentById(studentId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/enquiry/{enquiryId}")
    public ResponseEntity<StudentResponse> getStudentByEnquiryId(
            @PathVariable("enquiryId") Integer enquiryId) {

        StudentResponse response =
                studentService.getStudentByEnquiryId(enquiryId);

        return ResponseEntity.ok(response);
    }

    // studentId / admissionId are exact-match lookups (unambiguous IDs);
    // name / mobile remain partial/exact matches as before. Any
    // combination of query params is accepted - studentId and admissionId
    // just take priority when present.
    @GetMapping("/search")
    public ResponseEntity<List<StudentResponse>> searchStudents(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "mobile", required = false) Long mobile,
            @RequestParam(value = "studentId", required = false) Integer studentId,
            @RequestParam(value = "admissionId", required = false) Integer admissionId) {

        List<StudentResponse> students =
                studentService.searchStudents(name, mobile, studentId, admissionId);

        return ResponseEntity.ok(students);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable("studentId") Integer studentId,
            @Valid @RequestBody StudentRequest request) {

        StudentResponse response =
                studentService.updateStudent(studentId, request);

        return ResponseEntity.ok(response);
    }
}