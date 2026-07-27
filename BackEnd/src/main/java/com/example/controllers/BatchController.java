package com.example.controllers;


import com.example.dto.BatchRequest;
import com.example.dto.BatchResponse;
import com.example.services.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batches")
public class BatchController {

    @Autowired
    private BatchService batchService;

    @PostMapping
    public ResponseEntity<BatchResponse> createBatch(@RequestBody BatchRequest batchRequest) {
        return new ResponseEntity<>(batchService.createBatch(batchRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BatchResponse>> getAllBatches() {
        return ResponseEntity.ok(batchService.getAllBatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchResponse> getBatchById(@PathVariable Integer id) {
        return ResponseEntity.ok(batchService.getBatchById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BatchResponse> updateBatch(@PathVariable Integer id, @RequestBody BatchRequest batchRequest) {
        return ResponseEntity.ok(batchService.updateBatch(id, batchRequest));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BatchResponse> updateBatchStatus(@PathVariable Integer id, @RequestParam Boolean active) {
        return ResponseEntity.ok(batchService.updateBatchStatus(id, active));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<BatchResponse>> getBatchesByCourseId(@PathVariable Integer courseId) {
        return ResponseEntity.ok(batchService.getBatchesByCourseId(courseId));
    }

    @GetMapping("/course/{courseId}/active")
    public ResponseEntity<List<BatchResponse>> getActiveBatchesByCourseId(@PathVariable Integer courseId) {
        return ResponseEntity.ok(batchService.getActiveBatchesByCourseId(courseId));
    }
}