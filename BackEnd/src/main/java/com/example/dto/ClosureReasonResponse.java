package com.example.dto;

public class ClosureReasonResponse {
    private Integer closureReasonId;
    private String closureReasonDesc;

    public ClosureReasonResponse() {}

    public Integer getClosureReasonId() { return closureReasonId; }
    public void setClosureReasonId(Integer closureReasonId) { this.closureReasonId = closureReasonId; }

    public String getClosureReasonDesc() { return closureReasonDesc; }
    public void setClosureReasonDesc(String closureReasonDesc) { this.closureReasonDesc = closureReasonDesc; }
}