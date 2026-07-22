package com.example.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "followup")
public class Followup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "followup_id")
    private Integer followupId;

    @Column(name = "enquiry_id")
    private Integer enquiryId;

    @Column(name = "staff_id")
    private Integer staffId;

    @Column(name = "followup_date")
    private LocalDate followupDate;

    @Column(name = "followup_msg", length = 500)
    private String followupMsg;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Default Constructor
    public Followup() {
    }

    // Parameterized Constructor
    public Followup(Integer followupId, Integer enquiryId, Integer staffId,
                    LocalDate followupDate, String followupMsg, Boolean isActive) {
        this.followupId = followupId;
        this.enquiryId = enquiryId;
        this.staffId = staffId;
        this.followupDate = followupDate;
        this.followupMsg = followupMsg;
        this.isActive = isActive;
    }

    public Integer getFollowupId() {
        return followupId;
    }

    public void setFollowupId(Integer followupId) {
        this.followupId = followupId;
    }

    public Integer getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(Integer enquiryId) {
        this.enquiryId = enquiryId;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public LocalDate getFollowupDate() {
        return followupDate;
    }

    public void setFollowupDate(LocalDate followupDate) {
        this.followupDate = followupDate;
    }

    public String getFollowupMsg() {
        return followupMsg;
    }

    public void setFollowupMsg(String followupMsg) {
        this.followupMsg = followupMsg;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "Followup{" +
                "followupId=" + followupId +
                ", enquiryId=" + enquiryId +
                ", staffId=" + staffId +
                ", followupDate=" + followupDate +
                ", followupMsg='" + followupMsg + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}