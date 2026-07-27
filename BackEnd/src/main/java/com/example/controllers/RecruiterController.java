package com.example.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dto.RecruiterRequest;
import com.example.dto.RecruiterResponse;
import com.example.services.RecruiterService;

@RestController
@RequestMapping("/api/recruiters")
@CrossOrigin("*")
public class RecruiterController {

    @Autowired
    private RecruiterService recruiterService;

    @PostMapping
    public ResponseEntity<RecruiterResponse> addRecruiter(
            @RequestBody RecruiterRequest recruiterRequest) {

        return new ResponseEntity<>(
                recruiterService.addRecruiter(recruiterRequest),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecruiterResponse> getRecruiterById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                recruiterService.getRecruiterById(id));
    }

    @GetMapping
    public ResponseEntity<List<RecruiterResponse>> getAllRecruiters() {

        return ResponseEntity.ok(
                recruiterService.getAllRecruiters());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecruiterResponse> updateRecruiter(
            @PathVariable Integer id,
            @RequestBody RecruiterRequest recruiterRequest) {

        return ResponseEntity.ok(
                recruiterService.updateRecruiter(id,
                        recruiterRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecruiter(
            @PathVariable Integer id) {

        recruiterService.deleteRecruiter(id);

        return ResponseEntity.ok("Recruiter deleted successfully.");
    }

}