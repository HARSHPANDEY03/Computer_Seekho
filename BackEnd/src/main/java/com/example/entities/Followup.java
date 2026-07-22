package com.example.entities;

import java.time.LocalDate;	
	
import com.example.entities.Enquiry;
import com.example.entities.Staff;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "followup")
public class Followup {

    /*
     * followup_id
     * INT, Primary Key, Auto Increment
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "followup_id", nullable = false)
    private Integer followupId;

    /*
     * enquiry_id
     * Many follow-ups can belong to one enquiry.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enquiry_id")
    @JsonIgnoreProperties({
        "followups",
        "student",
        "hibernateLazyInitializer",
        "handler"
    })
    private Enquiry enquiry;

    /*
     * staff_id
     * One staff member can handle multiple follow-ups.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    @JsonIgnoreProperties({
        "followups",
        "enquiries",
        "hibernateLazyInitializer",
        "handler"
    })
    private Staff staff;

    /*
     * followup_date
     * MySQL DATE maps to Java LocalDate.
     */
    @Column(name = "followup_date")
    private LocalDate followupDate;

    /*
     * followup_msg
     * Maximum length: 500 characters.
     */
    @Column(name = "followup_msg", length = 500)
    private String followupMsg;

    /*
     * is_active
     * Default value is true.
     */
    @Column(name = "is_active")
    private Boolean isActive = true;

    // Required by JPA
    public Followup() {
    }

    public Followup(
            Enquiry enquiry,
            Staff staff,
            LocalDate followupDate,
            String followupMsg,
            Boolean isActive) {

        this.enquiry = enquiry;
        this.staff = staff;
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

    public Enquiry getEnquiry() {
        return enquiry;
    }

    public void setEnquiry(Enquiry enquiry) {
        this.enquiry = enquiry;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
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
                ", followupDate=" + followupDate +
                ", followupMsg='" + followupMsg + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}