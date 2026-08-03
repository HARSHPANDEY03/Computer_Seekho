package com.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "recruiter")
public class Recruiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruiter_id", nullable = false)
    private Integer recruiterId;

    /*
     * photo_url
     * Was VARCHAR(255) - widened to LONGTEXT so this can hold either a
     * short hosted image URL (unchanged, existing behaviour) OR a
     * base64 data: URL from the admin "Browse..." file picker.
     */
    @Column(name = "photo_url", columnDefinition = "LONGTEXT")
    private String photoUrl;

    @Column(name = "recruiter_name", length = 100)
    private String recruiterName;

    @Column(name = "description", length = 500)
    private String description;

    public Recruiter() {
    }

    public Recruiter(
            String photoUrl,
            String recruiterName,
            String description) {

        this.photoUrl = photoUrl;
        this.recruiterName = recruiterName;
        this.description = description;
    }

    public Integer getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(Integer recruiterId) {
        this.recruiterId = recruiterId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getRecruiterName() {
        return recruiterName;
    }

    public void setRecruiterName(String recruiterName) {
        this.recruiterName = recruiterName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Recruiter{" +
                "recruiterId=" + recruiterId +
                ", photoUrl='" + photoUrl + '\'' +
                ", recruiterName='" + recruiterName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}