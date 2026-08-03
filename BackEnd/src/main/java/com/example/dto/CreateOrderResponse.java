package com.example.dto;

public class CreateOrderResponse {

    private String razorpayOrderId;
    private Long amountInPaise;
    private String currency;
    private String keyId; // public key only - NEVER the secret
    private Integer courseId;
    private Integer batchId;
    private String courseName;

    public CreateOrderResponse() {
    }

    public CreateOrderResponse(String razorpayOrderId, Long amountInPaise, String currency,
                                String keyId, Integer courseId, Integer batchId, String courseName) {
        this.razorpayOrderId = razorpayOrderId;
        this.amountInPaise = amountInPaise;
        this.currency = currency;
        this.keyId = keyId;
        this.courseId = courseId;
        this.batchId = batchId;
        this.courseName = courseName;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public Long getAmountInPaise() {
        return amountInPaise;
    }

    public void setAmountInPaise(Long amountInPaise) {
        this.amountInPaise = amountInPaise;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
