package com.example.entities;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "placed_student")
public class PlacedStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "placed_student_id")
    private Integer placedStudentId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "recruiter_id", nullable = false)
    private Recruiter recruiter;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "job_role")
    private String jobRole;

    @Column(name = "package_amount")
    private Double packageAmount;

    @Column(name = "placement_date")
    private LocalDate placementDate;

    @Column(name = "location")
    private String location;

    @Column(name = "remarks")
    private String remarks;

    public PlacedStudent() {
    }

    public PlacedStudent(Integer placedStudentId, Student student, Recruiter recruiter,
                         String companyName, String jobRole, Double packageAmount,
                         LocalDate placementDate, String location, String remarks) {
        this.placedStudentId = placedStudentId;
        this.student = student;
        this.recruiter = recruiter;
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.packageAmount = packageAmount;
        this.placementDate = placementDate;
        this.location = location;
        this.remarks = remarks;
    }

    public Integer getPlacedStudentId() {
        return placedStudentId;
    }

    public void setPlacedStudentId(Integer placedStudentId) {
        this.placedStudentId = placedStudentId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Recruiter getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(Recruiter recruiter) {
        this.recruiter = recruiter;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public Double getPackageAmount() {
        return packageAmount;
    }

    public void setPackageAmount(Double packageAmount) {
        this.packageAmount = packageAmount;
    }

    public LocalDate getPlacementDate() {
        return placementDate;
    }

    public void setPlacementDate(LocalDate placementDate) {
        this.placementDate = placementDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "PlacedStudent [placedStudentId=" + placedStudentId +
                ", student=" + student +
                ", recruiter=" + recruiter +
                ", companyName=" + companyName +
                ", jobRole=" + jobRole +
                ", packageAmount=" + packageAmount +
                ", placementDate=" + placementDate +
                ", location=" + location +
                ", remarks=" + remarks + "]";
    }
}