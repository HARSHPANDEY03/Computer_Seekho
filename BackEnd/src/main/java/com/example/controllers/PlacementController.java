package com.example.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dto.PlacementRequest;
import com.example.dto.PlacementResponse;
import com.example.services.PlacementService;

@RestController
@RequestMapping("/api/placements")
@CrossOrigin("*")
public class PlacementController {

    @Autowired
    private PlacementService placementService;

    @PostMapping
    public ResponseEntity<PlacementResponse> addPlacement(
            @RequestBody PlacementRequest placementRequest) {

        return new ResponseEntity<>(
                placementService.addPlacement(
                        placementRequest),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlacementResponse> getPlacementById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                placementService.getPlacementById(id));
    }

    @GetMapping
    public ResponseEntity<List<PlacementResponse>> getAllPlacements() {

        return ResponseEntity.ok(
                placementService.getAllPlacements());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlacementResponse> updatePlacement(
            @PathVariable Integer id,
            @RequestBody PlacementRequest placementRequest) {

        return ResponseEntity.ok(
                placementService.updatePlacement(id,
                        placementRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlacement(
            @PathVariable Integer id) {

        placementService.deletePlacement(id);

        return ResponseEntity.ok(
                "Placement deleted successfully.");
    }

    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<List<PlacementResponse>>
    getPlacementsByRecruiter(
            @PathVariable Integer recruiterId) {

        return ResponseEntity.ok(
                placementService
                        .getPlacementsByRecruiter(
                                recruiterId));
    }

    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<PlacementResponse>>
    getPlacementsByBatch(
            @PathVariable Integer batchId) {

        return ResponseEntity.ok(
                placementService
                        .getPlacementsByBatch(batchId));
    }

}