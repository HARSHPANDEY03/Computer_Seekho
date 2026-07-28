package com.example.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class StudentRequest {

    @NotNull(message = "Enquiry ID is required")
    private Integer enquiryId;

    @NotBlank(message = "Student name is required")
    @Size(max = 100)
    private String studentName;

    @Size(max = 255)
    private String studentAddress;

    @Size(max = 10)
    private String studentGender;

    private String photoUrl;

    @Past(message = "Date of birth must be in the past")
    private LocalDate studentDob;

    @Size(max = 50)
    private String studentQualification;

    private Long studentMobile;

    private BigDecimal courseFee;

    @NotNull(message = "Batch ID is required")
    private Integer batchId;

    @NotNull(message = "Course ID is required")
    private Integer courseId;

    @Size(max = 100)
    private String studentUsername;

    private String studentPassword;

    public StudentRequest() {
    }

    public Integer getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(Integer enquiryId) {
        this.enquiryId = enquiryId;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
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

    public BigDecimal getCourseFee() {
        return courseFee;
    }

    public void setCourseFee(BigDecimal courseFee) {
        this.courseFee = courseFee;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getStudentPassword() {
        return studentPassword;
    }

    public void setStudentPassword(String studentPassword) {
        this.studentPassword = studentPassword;
    }
}