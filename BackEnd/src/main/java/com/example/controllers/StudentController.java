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

    // Constructor injection
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Register a new student
    // POST: http://localhost:8080/api/students/register
    @PostMapping("/register")
    public ResponseEntity<StudentResponse> registerStudent(
            @Valid @RequestBody StudentRequest request) {

        StudentResponse response =
                studentService.registerStudent(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get all students
    // GET: http://localhost:8080/api/students
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {

        List<StudentResponse> students =
                studentService.getAllStudents();

        return ResponseEntity.ok(students);
    }

    // Get student using student ID
    // GET: http://localhost:8080/api/students/1
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponse> getStudentById(
            @PathVariable Integer studentId) {

        StudentResponse response =
                studentService.getStudentById(studentId);

        return ResponseEntity.ok(response);
    }

    // Get student using enquiry ID
    // GET: http://localhost:8080/api/students/enquiry/1
    @GetMapping("/enquiry/{enquiryId}")
    public ResponseEntity<StudentResponse> getStudentByEnquiryId(
            @PathVariable Integer enquiryId) {

        StudentResponse response =
                studentService.getStudentByEnquiryId(enquiryId);

        return ResponseEntity.ok(response);
    }

    // Search students using name, mobile, or both
    //
    // GET: /api/students/search?name=Vaishnavi
    // GET: /api/students/search?mobile=9876543210
    // GET: /api/students/search?name=Vaishnavi&mobile=9876543210
    @GetMapping("/search")
    public ResponseEntity<List<StudentResponse>> searchStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long mobile) {

        List<StudentResponse> students =
                studentService.searchStudents(name, mobile);

        return ResponseEntity.ok(students);
    }

    // Update an existing student
    // PUT: http://localhost:8080/api/students/1
    @PutMapping("/{studentId}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Integer studentId,
            @Valid @RequestBody StudentRequest request) {

        StudentResponse response =
                studentService.updateStudent(studentId, request);

        return ResponseEntity.ok(response);
    }
}