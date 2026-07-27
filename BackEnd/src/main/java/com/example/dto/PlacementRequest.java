package com.example.dto;

import java.math.BigDecimal;

public class PlacementRequest {

    private String placedStudentName;

    private BigDecimal placementPackage;

    private Integer recruiterId;

    private Integer batchId;

    public PlacementRequest() {
    }

    public String getPlacedStudentName() {
        return placedStudentName;
    }

    public void setPlacedStudentName(String placedStudentName) {
        this.placedStudentName = placedStudentName;
    }

    public BigDecimal getPlacementPackage() {
        return placementPackage;
    }

    public void setPlacementPackage(BigDecimal placementPackage) {
        this.placementPackage = placementPackage;
    }

    public Integer getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(Integer recruiterId) {
        this.recruiterId = recruiterId;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }
}