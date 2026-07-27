package com.example.dto;

import java.time.LocalDate;

public class EnquirySearchResponse {
    private Integer enquiryId;
    private String enquirerName;
    private String enquirerMobile;
    private String enquirerEmailId;
    private LocalDate enquiryDate;
    private Boolean enquiryProcessedFlag;

    public EnquirySearchResponse() {}

    // Getters and Setters
    public Integer getEnquiryId() { return enquiryId; }
    public void setEnquiryId(Integer enquiryId) { this.enquiryId = enquiryId; }

    public String getEnquirerName() { return enquirerName; }
    public void setEnquirerName(String enquirerName) { this.enquirerName = enquirerName; }

    public String getEnquirerMobile() { return enquirerMobile; }
    public void setEnquirerMobile(String enquirerMobile) { this.enquirerMobile = enquirerMobile; }

    public String getEnquirerEmailId() { return enquirerEmailId; }
    public void setEnquirerEmailId(String enquirerEmailId) { this.enquirerEmailId = enquirerEmailId; }

    public LocalDate getEnquiryDate() { return enquiryDate; }
    public void setEnquiryDate(LocalDate enquiryDate) { this.enquiryDate = enquiryDate; }

    public Boolean getEnquiryProcessedFlag() { return enquiryProcessedFlag; }
    public void setEnquiryProcessedFlag(Boolean enquiryProcessedFlag) { this.enquiryProcessedFlag = enquiryProcessedFlag; }
}