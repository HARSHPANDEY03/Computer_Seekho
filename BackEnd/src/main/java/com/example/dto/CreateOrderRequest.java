package com.example.dto;

public class CreateOrderRequest {

    private Integer courseId;
    private Integer batchId; // optional, nullable

    // --- Installment support ---
    // studentId: set when this is a follow-up payment against an already
    // admitted student, rather than the first/admitting payment.
    private Integer studentId;
    // amount: the amount to charge THIS time. Optional - if omitted on a
    // first payment, the full course/batch fee is charged (old behaviour).
    // Required when studentId is set. Always validated server-side against
    // the remaining pending balance - the browser's number is never trusted
    // outright, only checked against what's actually still owed.
    private Double amount;

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

    // Only used on a first payment (studentId absent) - lets createOrder
    // reject an already-admitted enquiry BEFORE money changes hands,
    // instead of only catching it after Razorpay has already charged the
    // student at verify time.
    private Integer enquiryId;

    public Integer getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(Integer enquiryId) {
        this.enquiryId = enquiryId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}