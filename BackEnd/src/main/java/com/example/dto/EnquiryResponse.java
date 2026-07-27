package com.example.dto;

import java.time.LocalDate;

public class EnquiryResponse {
    private Integer enquiryId;
    private String enquirerName;
    private String enquirerAddress;
    private Long enquirerMobile;
    private Long enquirerAlternateMobile;
    private String enquirerEmailId;
    private LocalDate enquiryDate;
    private String enquirerQuery;
    private String closureReasonText;
    private Boolean enquiryProcessedFlag;
    private Integer inquiryCounter;
    private LocalDate followupDate;
    private String enquirySource;
    private ClosureReasonResponse closureReason;

    public EnquiryResponse() {}

    // Getters and Setters
    public Integer getEnquiryId() { return enquiryId; }
    public void setEnquiryId(Integer enquiryId) { this.enquiryId = enquiryId; }

    public String getEnquirerName() { return enquirerName; }
    public void setEnquirerName(String enquirerName) { this.enquirerName = enquirerName; }

    public String getEnquirerAddress() { return enquirerAddress; }
    public void setEnquirerAddress(String enquirerAddress) { this.enquirerAddress = enquirerAddress; }

    public Long getEnquirerMobile() { return enquirerMobile; }
    public void setEnquirerMobile(Long enquirerMobile) { this.enquirerMobile = enquirerMobile; }

    public Long getEnquirerAlternateMobile() { return enquirerAlternateMobile; }
    public void setEnquirerAlternateMobile(Long enquirerAlternateMobile) { this.enquirerAlternateMobile = enquirerAlternateMobile; }

    public String getEnquirerEmailId() { return enquirerEmailId; }
    public void setEnquirerEmailId(String enquirerEmailId) { this.enquirerEmailId = enquirerEmailId; }

    public LocalDate getEnquiryDate() { return enquiryDate; }
    public void setEnquiryDate(LocalDate enquiryDate) { this.enquiryDate = enquiryDate; }

    public String getEnquirerQuery() { return enquirerQuery; }
    public void setEnquirerQuery(String enquirerQuery) { this.enquirerQuery = enquirerQuery; }

    public String getClosureReasonText() { return closureReasonText; }
    public void setClosureReasonText(String closureReasonText) { this.closureReasonText = closureReasonText; }

    public Boolean getEnquiryProcessedFlag() { return enquiryProcessedFlag; }
    public void setEnquiryProcessedFlag(Boolean enquiryProcessedFlag) { this.enquiryProcessedFlag = enquiryProcessedFlag; }

    public Integer getInquiryCounter() { return inquiryCounter; }
    public void setInquiryCounter(Integer inquiryCounter) { this.inquiryCounter = inquiryCounter; }

    public LocalDate getFollowupDate() { return followupDate; }
    public void setFollowupDate(LocalDate followupDate) { this.followupDate = followupDate; }

    public String getEnquirySource() { return enquirySource; }
    public void setEnquirySource(String enquirySource) { this.enquirySource = enquirySource; }

    public ClosureReasonResponse getClosureReason() { return closureReason; }
    public void setClosureReason(ClosureReasonResponse closureReason) { this.closureReason = closureReason; }
}