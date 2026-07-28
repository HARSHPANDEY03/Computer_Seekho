package com.example.services;

import java.util.List;

import com.example.dto.StudentRequest;
import com.example.dto.StudentResponse;

public interface StudentService {

    StudentResponse registerStudent(StudentRequest request);

    List<StudentResponse> getAllStudents();
    

    StudentResponse getStudentById(Integer studentId);

    StudentResponse getStudentByEnquiryId(Integer enquiryId);

    List<StudentResponse> searchStudents(
            String name,
            Long mobile);

    StudentResponse updateStudent(  Integer studentId,   StudentRequest request);
}
