package com.example.services;

import java.util.List;

import com.example.dto.StudentRequest;
import com.example.dto.StudentResponse;

public interface StudentService {

    StudentResponse registerStudent(StudentRequest request);

    List<StudentResponse> getAllStudents();


    StudentResponse getStudentById(Integer studentId);

    StudentResponse getStudentByEnquiryId(Integer enquiryId);

    // studentId and admissionId (enquiryId), when given, are exact-match
    // lookups and take priority over the name/mobile partial search below -
    // an ID is unambiguous, unlike a name or number.
    List<StudentResponse> searchStudents(
            String name,
            Long mobile,
            Integer studentId,
            Integer admissionId);

StudentResponse updateStudent(
            Integer studentId,
            StudentRequest request);
}