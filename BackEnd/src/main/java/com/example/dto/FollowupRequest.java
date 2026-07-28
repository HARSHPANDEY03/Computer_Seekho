package com.example.dto;

import java.time.LocalDate;

public class FollowupRequest {

    private Integer enquiryId;
    private String followupMsg;
    private LocalDate nextFollowupDate;
    private Integer closureReasonId;
    private String closureReasonText;

    public FollowupRequest() {
    }

    public Integer getEnquiryId() { return enquiryId; }
    public void setEnquiryId(Integer enquiryId) { this.enquiryId = enquiryId; }
    public String getFollowupMsg() { return followupMsg; }
    public void setFollowupMsg(String followupMsg) { this.followupMsg = followupMsg; }
    public LocalDate getNextFollowupDate() { return nextFollowupDate; }
    public void setNextFollowupDate(LocalDate nextFollowupDate) { this.nextFollowupDate = nextFollowupDate; }
    public Integer getClosureReasonId() { return closureReasonId; }
    public void setClosureReasonId(Integer closureReasonId) { this.closureReasonId = closureReasonId; }
    public String getClosureReasonText() { return closureReasonText; }
    public void setClosureReasonText(String closureReasonText) { this.closureReasonText = closureReasonText; }
}