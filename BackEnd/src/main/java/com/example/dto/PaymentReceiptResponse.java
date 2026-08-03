package com.example.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentReceiptResponse {

    private Integer receiptId;
    private LocalDate receiptDate;
    private BigDecimal receiptAmount;
    private Integer studentId;
    private String studentName;
    private String studentEmail;
    private Long studentMobile;
    private Integer courseId;
    private String courseName;
    private Integer batchId;
    private String batchName;
    private String razorpayPaymentId;
    private Integer paymentId;
    private boolean emailSent;

    public PaymentReceiptResponse() {
    }

    public PaymentReceiptResponse(Integer receiptId, LocalDate receiptDate, BigDecimal receiptAmount,
                                   Integer studentId, String studentName, String studentEmail, Long studentMobile,
                                   Integer courseId, String courseName, Integer batchId, String batchName,
                                   String razorpayPaymentId, Integer paymentId, boolean emailSent) {
        this.receiptId = receiptId;
        this.receiptDate = receiptDate;
        this.receiptAmount = receiptAmount;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentMobile = studentMobile;
        this.courseId = courseId;
        this.courseName = courseName;
        this.batchId = batchId;
        this.batchName = batchName;
        this.razorpayPaymentId = razorpayPaymentId;
        this.paymentId = paymentId;
        this.emailSent = emailSent;
    }

    public Integer getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Integer receiptId) {
        this.receiptId = receiptId;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }

    public BigDecimal getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(BigDecimal receiptAmount) {
        this.receiptAmount = receiptAmount;
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

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public Long getStudentMobile() {
        return studentMobile;
    }

    public void setStudentMobile(Long studentMobile) {
        this.studentMobile = studentMobile;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public boolean isEmailSent() {
        return emailSent;
    }

    public void setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
    }
}
