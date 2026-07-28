package com.example.dto;

import java.math.BigDecimal;

public class PlacementResponse {

    private Integer placedStudentId;

    private String placedStudentName;

    private BigDecimal placementPackage;

    private Integer recruiterId;

    private String recruiterName;

    private Integer batchId;

    private String batchName;

    public PlacementResponse() {
    }

    public Integer getPlacedStudentId() {
        return placedStudentId;
    }

    public void setPlacedStudentId(Integer placedStudentId) {
        this.placedStudentId = placedStudentId;
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

    public String getRecruiterName() {
        return recruiterName;
    }

    public void setRecruiterName(String recruiterName) {
        this.recruiterName = recruiterName;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }
}