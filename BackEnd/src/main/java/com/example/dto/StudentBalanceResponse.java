package com.example.dto;

import java.math.BigDecimal;

public class StudentBalanceResponse {

    private Integer studentId;
    private String studentName;
    private BigDecimal courseFee;
    private Double totalPaid;
    private Double pendingAmount;

    public StudentBalanceResponse() {
    }

    public StudentBalanceResponse(Integer studentId, String studentName, BigDecimal courseFee,
                                   Double totalPaid, Double pendingAmount) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseFee = courseFee;
        this.totalPaid = totalPaid;
        this.pendingAmount = pendingAmount;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public BigDecimal getCourseFee() {
        return courseFee;
    }

    public void setCourseFee(BigDecimal courseFee) {
        this.courseFee = courseFee;
    }

    public Double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(Double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public Double getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(Double pendingAmount) {
        this.pendingAmount = pendingAmount;
    }
}