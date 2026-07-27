package com.example.dto;

public class AssignStaffRequest {
    private Integer staffId;
    private String remarks;

    public AssignStaffRequest() {}

    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}