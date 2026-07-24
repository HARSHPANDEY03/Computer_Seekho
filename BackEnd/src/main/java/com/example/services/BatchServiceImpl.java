package com.example.services;



import com.example.dto.BatchRequest;
import com.example.dto.BatchResponse;
import com.example.entities.Batch;
import com.example.entities.Course;
import com.example.repositories.BatchRepository;
import com.example.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public BatchResponse createBatch(BatchRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + request.getCourseId()));

        Batch batch = mapToEntity(request, course);
        Batch savedBatch = batchRepository.save(batch);
        return mapToResponse(savedBatch);
    }

    @Override
    public List<BatchResponse> getAllBatches() {
        return batchRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BatchResponse getBatchById(Integer id) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found with id: " + id));
        return mapToResponse(batch);
    }

    @Override
    public BatchResponse updateBatch(Integer id, BatchRequest request) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found with id: " + id));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + request.getCourseId()));

        batch.setBatchName(request.getBatchName());
        batch.setBatchStartTime(request.getBatchStartTime());
        batch.setBatchEndTime(request.getBatchEndTime());
        batch.setCourse(course);
        batch.setPresentationDate(request.getPresentationDate());
        batch.setCourseFees(request.getCourseFees());
        batch.setCourseFeesFrom(request.getCourseFeesFrom());
        batch.setCourseFeesTo(request.getCourseFeesTo());
        if (request.getBatchIsActive() != null) batch.setBatchIsActive(request.getBatchIsActive());

        Batch updatedBatch = batchRepository.save(batch);
        return mapToResponse(updatedBatch);
    }

    @Override
    public BatchResponse updateBatchStatus(Integer id, Boolean status) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found with id: " + id));
        batch.setBatchIsActive(status);
        Batch updatedBatch = batchRepository.save(batch);
        return mapToResponse(updatedBatch);
    }

    @Override
    public List<BatchResponse> getBatchesByCourseId(Integer courseId) {
        return batchRepository.findByCourseCourseId(courseId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BatchResponse> getActiveBatchesByCourseId(Integer courseId) {
        return batchRepository.findByCourseCourseIdAndBatchIsActiveTrue(courseId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private Batch mapToEntity(BatchRequest request, Course course) {
        Batch batch = new Batch();
        batch.setBatchName(request.getBatchName());
        batch.setBatchStartTime(request.getBatchStartTime());
        batch.setBatchEndTime(request.getBatchEndTime());
        batch.setCourse(course);
        batch.setPresentationDate(request.getPresentationDate());
        batch.setCourseFees(request.getCourseFees());
        batch.setCourseFeesFrom(request.getCourseFeesFrom());
        batch.setCourseFeesTo(request.getCourseFeesTo());
        batch.setBatchIsActive(request.getBatchIsActive() != null ? request.getBatchIsActive() : true);
        return batch;
    }

    private BatchResponse mapToResponse(Batch batch) {
        BatchResponse response = new BatchResponse();
        response.setBatchId(batch.getBatchId());
        response.setBatchName(batch.getBatchName());
        response.setBatchStartTime(batch.getBatchStartTime());
        response.setBatchEndTime(batch.getBatchEndTime());
        if (batch.getCourse() != null) {
            response.setCourseId(batch.getCourse().getCourseId());
            response.setCourseName(batch.getCourse().getCourseName());
        }
        response.setPresentationDate(batch.getPresentationDate());
        response.setCourseFees(batch.getCourseFees());
        response.setCourseFeesFrom(batch.getCourseFeesFrom());
        response.setCourseFeesTo(batch.getCourseFeesTo());
        response.setBatchIsActive(batch.getBatchIsActive());
        return response;
    }
}