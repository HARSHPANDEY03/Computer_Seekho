package com.example.dto;

import java.time.LocalDate;

public class VerifyPaymentRequest {

    // --- Razorpay checkout response fields (only used by the online flow) ---
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;

    // --- Student details collected in the payment form (mirrors the
    //     Student entity, minus student_username / student_password which
    //     this flow does not collect). Only used when studentId is null -
    //     i.e. this is the FIRST payment, which admits the student. ---
    private String studentName;
    private String studentAddress;
    private String studentGender;
    private LocalDate studentDob;
    private String studentQualification;
    private Long studentMobile;
    private String studentEmail; // optional - admission confirmation is sent here if present
    private String photoUrl; // optional

    private Integer courseId;
    private Integer batchId;
    private Integer enquiryId; // optional - links the new student back to an existing enquiry
 // Optional free-text note the admin can attach to this payment.
    // Works for both the offline and online flows.
    private String remarks;
    // --- Installment support ---
    // studentId: set when this is a follow-up payment against an already
    // admitted student. When present, no new Student row is created - the
    // student detail fields above and enquiryId are ignored entirely.
    private Integer studentId;

    // --- Offline (Cash / Bank) flow only - ignored by the online verify flow ---
    private String paymentType; // "Cash" or "Bank"
    private Double amount;

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public String getRazorpaySignature() {
        return razorpaySignature;
    }

    public void setRazorpaySignature(String razorpaySignature) {
        this.razorpaySignature = razorpaySignature;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentAddress() {
        return studentAddress;
    }

    public void setStudentAddress(String studentAddress) {
        this.studentAddress = studentAddress;
    }

    public String getStudentGender() {
        return studentGender;
    }

    public void setStudentGender(String studentGender) {
        this.studentGender = studentGender;
    }

    public LocalDate getStudentDob() {
        return studentDob;
    }

    public void setStudentDob(LocalDate studentDob) {
        this.studentDob = studentDob;
    }

    public String getStudentQualification() {
        return studentQualification;
    }

    public void setStudentQualification(String studentQualification) {
        this.studentQualification = studentQualification;
    }

    public Long getStudentMobile() {
        return studentMobile;
    }

    public void setStudentMobile(Long studentMobile) {
        this.studentMobile = studentMobile;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
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

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
