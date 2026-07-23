package com.example.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "recruiters")
public class Recruiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruiter_id")
    private Integer recruiterId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "hr_name", nullable = false)
    private String hrName;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "designation")
    private String designation;

    @Column(name = "address")
    private String address;

    public Recruiter() {
    }

    public Recruiter(Integer recruiterId, String companyName, String hrName,
                     String email, String mobile, String designation,
                     String address) {
        this.recruiterId = recruiterId;
        this.companyName = companyName;
        this.hrName = hrName;
        this.email = email;
        this.mobile = mobile;
        this.designation = designation;
        this.address = address;
    }

    public Integer getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(Integer recruiterId) {
        this.recruiterId = recruiterId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getHrName() {
        return hrName;
    }

    public void setHrName(String hrName) {
        this.hrName = hrName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}