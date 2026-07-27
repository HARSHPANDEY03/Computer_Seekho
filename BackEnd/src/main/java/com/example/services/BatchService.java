package com.example.services;

import com.example.dto.BatchRequest;
import com.example.dto.BatchResponse;

import java.util.List;

public interface BatchService {
    BatchResponse createBatch(BatchRequest batchRequest);
    List<BatchResponse> getAllBatches();
    BatchResponse getBatchById(Integer id);
    BatchResponse updateBatch(Integer id, BatchRequest batchRequest);
    BatchResponse updateBatchStatus(Integer id, Boolean status);
    List<BatchResponse> getBatchesByCourseId(Integer courseId);
    List<BatchResponse> getActiveBatchesByCourseId(Integer courseId);
}