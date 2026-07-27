package com.example.dto;

public class CloseEnquiryRequest {
    private Integer closureReasonId;
    private String closureReasonText;

    public CloseEnquiryRequest() {}

    public Integer getClosureReasonId() { return closureReasonId; }
    public void setClosureReasonId(Integer closureReasonId) { this.closureReasonId = closureReasonId; }

    public String getClosureReasonText() { return closureReasonText; }
    public void setClosureReasonText(String closureReasonText) { this.closureReasonText = closureReasonText; }
}