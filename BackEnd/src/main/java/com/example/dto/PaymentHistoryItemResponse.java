package com.example.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentHistoryItemResponse {

    private Integer paymentId;
    private Integer receiptId; // null for Pending/Failed rows - no receipt was ever generated
    private LocalDate paymentDate;
    private Double amountPaid;
    private BigDecimal remainingFeeAfter;
    private BigDecimal totalCourseFee;
    private String paymentMode;
    private String transactionId; // Razorpay payment ID, null for offline payments
    private String status; // "Success" | "Pending" | "Failed"
    private String collectedBy; // staff username, null for payments recorded before this field existed
    private String remarks;

    public PaymentHistoryItemResponse() {
    }

    public PaymentHistoryItemResponse(Integer paymentId, Integer receiptId, LocalDate paymentDate,
                                       Double amountPaid, BigDecimal remainingFeeAfter, BigDecimal totalCourseFee,
                                       String paymentMode, String transactionId, String status,
                                       String collectedBy, String remarks) {
        this.paymentId = paymentId;
        this.receiptId = receiptId;
        this.paymentDate = paymentDate;
        this.amountPaid = amountPaid;
        this.remainingFeeAfter = remainingFeeAfter;
        this.totalCourseFee = totalCourseFee;
        this.paymentMode = paymentMode;
        this.transactionId = transactionId;
        this.status = status;
        this.collectedBy = collectedBy;
        this.remarks = remarks;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Integer receiptId) {
        this.receiptId = receiptId;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public BigDecimal getRemainingFeeAfter() {
        return remainingFeeAfter;
    }

    public void setRemainingFeeAfter(BigDecimal remainingFeeAfter) {
        this.remainingFeeAfter = remainingFeeAfter;
    }

    public BigDecimal getTotalCourseFee() {
        return totalCourseFee;
    }

    public void setTotalCourseFee(BigDecimal totalCourseFee) {
        this.totalCourseFee = totalCourseFee;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCollectedBy() {
        return collectedBy;
    }

    public void setCollectedBy(String collectedBy) {
        this.collectedBy = collectedBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}