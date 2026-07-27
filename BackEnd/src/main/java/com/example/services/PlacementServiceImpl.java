package com.example.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.PlacementRequest;
import com.example.dto.PlacementResponse;
import com.example.entities.Batch;
import com.example.entities.PlacedStudent;
import com.example.entities.Recruiter;
import com.example.exceptions.PlacementNotFoundException;
import com.example.repositories.BatchRepository;
import com.example.repositories.PlacedStudentRepository;
import com.example.repositories.RecruiterRepository;

@Service
public class PlacementServiceImpl implements PlacementService {

    @Autowired
    private PlacedStudentRepository placedStudentRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Override
    public PlacementResponse addPlacement(PlacementRequest placementRequest) {

        Recruiter recruiter = recruiterRepository.findById(
                placementRequest.getRecruiterId())
                .orElseThrow(() ->
                        new RuntimeException("Recruiter not found"));

        Batch batch = batchRepository.findById(
                placementRequest.getBatchId())
                .orElseThrow(() ->
                        new RuntimeException("Batch not found"));

        PlacedStudent placedStudent = new PlacedStudent();

        placedStudent.setPlacedStudentName(
                placementRequest.getPlacedStudentName());

        placedStudent.setPlacementPackage(
                placementRequest.getPlacementPackage());

        placedStudent.setRecruiter(recruiter);

        placedStudent.setBatch(batch);

        PlacedStudent savedPlacement =
                placedStudentRepository.save(placedStudent);

        return mapToResponse(savedPlacement);
    }

    @Override
    public PlacementResponse getPlacementById(Integer placedStudentId) {

        PlacedStudent placedStudent =
                placedStudentRepository.findById(placedStudentId)
                .orElseThrow(() ->
                        new PlacementNotFoundException(
                                "Placement not found with id : "
                                        + placedStudentId));

        return mapToResponse(placedStudent);
    }

    @Override
    public List<PlacementResponse> getAllPlacements() {

        List<PlacedStudent> placedStudents =
                placedStudentRepository.findAll();

        List<PlacementResponse> responseList =
                new ArrayList<>();

        for (PlacedStudent placedStudent : placedStudents) {

            responseList.add(mapToResponse(placedStudent));

        }

        return responseList;
    }
    
    @Override
    public PlacementResponse updatePlacement(Integer placedStudentId,
                                             PlacementRequest placementRequest) {

        PlacedStudent placedStudent = placedStudentRepository
                .findById(placedStudentId)
                .orElseThrow(() ->
                        new PlacementNotFoundException(
                                "Placement not found with id : "
                                        + placedStudentId));

        Recruiter recruiter = recruiterRepository
                .findById(placementRequest.getRecruiterId())
                .orElseThrow(() ->
                        new RuntimeException("Recruiter not found"));

        Batch batch = batchRepository
                .findById(placementRequest.getBatchId())
                .orElseThrow(() ->
                        new RuntimeException("Batch not found"));

        placedStudent.setPlacedStudentName(
                placementRequest.getPlacedStudentName());

        placedStudent.setPlacementPackage(
                placementRequest.getPlacementPackage());

        placedStudent.setRecruiter(recruiter);

        placedStudent.setBatch(batch);

        PlacedStudent updatedPlacement =
                placedStudentRepository.save(placedStudent);

        return mapToResponse(updatedPlacement);
    }

    @Override
    public void deletePlacement(Integer placedStudentId) {

        PlacedStudent placedStudent =
                placedStudentRepository.findById(placedStudentId)
                .orElseThrow(() ->
                        new PlacementNotFoundException(
                                "Placement not found with id : "
                                        + placedStudentId));

        placedStudentRepository.delete(placedStudent);
    }

    @Override
    public List<PlacementResponse> getPlacementsByRecruiter(Integer recruiterId) {

        List<PlacedStudent> placedStudents =
                placedStudentRepository
                        .findByRecruiterRecruiterId(recruiterId);

        List<PlacementResponse> responseList =
                new ArrayList<>();

        for (PlacedStudent placedStudent : placedStudents) {

            responseList.add(mapToResponse(placedStudent));
        }

        return responseList;
    }

    @Override
    public List<PlacementResponse> getPlacementsByBatch(Integer batchId) {

        List<PlacedStudent> placedStudents =
                placedStudentRepository
                        .findByBatchBatchId(batchId);

        List<PlacementResponse> responseList =
                new ArrayList<>();

        for (PlacedStudent placedStudent : placedStudents) {

            responseList.add(mapToResponse(placedStudent));
        }

        return responseList;
    }

    private PlacementResponse mapToResponse(
            PlacedStudent placedStudent) {

        PlacementResponse response =
                new PlacementResponse();

        response.setPlacedStudentId(
                placedStudent.getPlacedStudentId());

        response.setPlacedStudentName(
                placedStudent.getPlacedStudentName());

        response.setPlacementPackage(
                placedStudent.getPlacementPackage());

        if (placedStudent.getRecruiter() != null) {

            response.setRecruiterId(
                    placedStudent.getRecruiter().getRecruiterId());

            response.setRecruiterName(
                    placedStudent.getRecruiter().getRecruiterName());
        }

        if (placedStudent.getBatch() != null) {

            response.setBatchId(
                    placedStudent.getBatch().getBatchId());

            response.setBatchName(
                    placedStudent.getBatch().getBatchName());
        }

        return response;
    }

}