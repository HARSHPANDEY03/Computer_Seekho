package com.example.dto;

import java.time.LocalDate;

public class PaymentResponseDTO {

    private Integer paymentId;
    private Integer paymentTypeId;
    private String paymentTypeDesc;
    private LocalDate paymentDate;
    private Integer studentId;
    private String studentName;
    private Integer courseId;
    private String courseName;
    private Integer batchId;
    private String batchName;
    private Double amount;

    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }

    public Integer getPaymentTypeId() { return paymentTypeId; }
    public void setPaymentTypeId(Integer paymentTypeId) { this.paymentTypeId = paymentTypeId; }

    public String getPaymentTypeDesc() { return paymentTypeDesc; }
    public void setPaymentTypeDesc(String paymentTypeDesc) { this.paymentTypeDesc = paymentTypeDesc; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public Integer getBatchId() { return batchId; }
    public void setBatchId(Integer batchId) { this.batchId = batchId; }

    public String getBatchName() { return batchName; }
    public void setBatchName(String batchName) { this.batchName = batchName; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}