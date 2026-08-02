package com.example.services;

import java.util.List;

import com.example.dto.PlacementRequest;
import com.example.dto.PlacementResponse;

public interface PlacementService {

    PlacementResponse addPlacement(PlacementRequest placementRequest);

    PlacementResponse getPlacementById(Integer placedStudentId);

    List<PlacementResponse> getAllPlacements();

    PlacementResponse updatePlacement(Integer placedStudentId,
                                      PlacementRequest placementRequest);

    void deletePlacement(Integer placedStudentId);

    List<PlacementResponse> getPlacementsByRecruiter(Integer recruiterId);

    List<PlacementResponse> getPlacementsByBatch(Integer batchId);

}