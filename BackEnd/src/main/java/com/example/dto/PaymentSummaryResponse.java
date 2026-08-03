package com.example.dto;

import java.math.BigDecimal;

public class PaymentSummaryResponse {

    private Integer studentId;
    private String studentName;
    private BigDecimal courseFee;
    private BigDecimal totalPaid;
    private BigDecimal pendingAmount;
    private boolean fullyPaid;

    public PaymentSummaryResponse() {
    }

    public PaymentSummaryResponse(Integer studentId, String studentName, BigDecimal courseFee,
                                   BigDecimal totalPaid, BigDecimal pendingAmount, boolean fullyPaid) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseFee = courseFee;
        this.totalPaid = totalPaid;
        this.pendingAmount = pendingAmount;
        this.fullyPaid = fullyPaid;
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

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public BigDecimal getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(BigDecimal pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public boolean isFullyPaid() {
        return fullyPaid;
    }

    public void setFullyPaid(boolean fullyPaid) {
        this.fullyPaid = fullyPaid;
    }
}
