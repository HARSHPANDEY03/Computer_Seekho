package com.example.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dto.StudentRequest;
import com.example.dto.StudentResponse;

@Service
public class StudentServiceImpl implements StudentService {

    @Override
    public StudentResponse registerStudent(StudentRequest request) {

        // TODO: Save student using repository

        return new StudentResponse();
    }

    @Override
    public List<StudentResponse> getAllStudents() {

        // TODO: Fetch all students

        return new ArrayList<>();
    }

    @Override
    public StudentResponse getStudentById(Integer studentId) {

        // TODO: Fetch student by id

        return new StudentResponse();
    }

    @Override
    public StudentResponse getStudentByEnquiryId(Integer enquiryId) {

        // TODO: Fetch student by enquiry id

        return new StudentResponse();
    }

    @Override
    public List<StudentResponse> searchStudents(String name, Long mobile) {

        // TODO: Search students

        return new ArrayList<>();
    }

    @Override
    public StudentResponse updateStudent(Integer studentId, StudentRequest request) {

        // TODO: Update student

        return new StudentResponse();
    }

}