package com.example.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "batch")
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_id")
    private Integer batchId;

    @Column(name = "batch_name", length = 100)
    private String batchName;

    @Column(name = "batch_start_time")
    private LocalTime batchStartTime;

    @Column(name = "batch_end_time")
    private LocalTime batchEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonBackReference
    private Course course;

    @Column(name = "presentation_date")
    private LocalDateTime presentationDate;

    @Column(name = "course_fees", precision = 10, scale = 2)
    private BigDecimal courseFees;

    @Column(name = "course_fees_from")
    private LocalDate courseFeesFrom;

    @Column(name = "course_fees_to")
    private LocalDate courseFeesTo;

    @Column(name = "batch_is_active")
    private Boolean batchIsActive = true;

    public Batch() {
    }

    public Batch(Integer batchId, String batchName, LocalTime batchStartTime,
                 LocalTime batchEndTime, Course course,
                 LocalDateTime presentationDate, BigDecimal courseFees,
                 LocalDate courseFeesFrom, LocalDate courseFeesTo,
                 Boolean batchIsActive) {
        this.batchId = batchId;
        this.batchName = batchName;
        this.batchStartTime = batchStartTime;
        this.batchEndTime = batchEndTime;
        this.course = course;
        this.presentationDate = presentationDate;
        this.courseFees = courseFees;
        this.courseFeesFrom = courseFeesFrom;
        this.courseFeesTo = courseFeesTo;
        this.batchIsActive = batchIsActive;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public LocalTime getBatchStartTime() {
        return batchStartTime;
    }

    public void setBatchStartTime(LocalTime batchStartTime) {
        this.batchStartTime = batchStartTime;
    }

    public LocalTime getBatchEndTime() {
        return batchEndTime;
    }

    public void setBatchEndTime(LocalTime batchEndTime) {
        this.batchEndTime = batchEndTime;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDateTime getPresentationDate() {
        return presentationDate;
    }

    public void setPresentationDate(LocalDateTime presentationDate) {
        this.presentationDate = presentationDate;
    }

    public BigDecimal getCourseFees() {
        return courseFees;
    }

    public void setCourseFees(BigDecimal courseFees) {
        this.courseFees = courseFees;
    }

    public LocalDate getCourseFeesFrom() {
        return courseFeesFrom;
    }

    public void setCourseFeesFrom(LocalDate courseFeesFrom) {
        this.courseFeesFrom = courseFeesFrom;
    }

    public LocalDate getCourseFeesTo() {
        return courseFeesTo;
    }

    public void setCourseFeesTo(LocalDate courseFeesTo) {
        this.courseFeesTo = courseFeesTo;
    }

    public Boolean getBatchIsActive() {
        return batchIsActive;
    }

    public void setBatchIsActive(Boolean batchIsActive) {
        this.batchIsActive = batchIsActive;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "batchId=" + batchId +
                ", batchName='" + batchName + '\'' +
                ", batchStartTime=" + batchStartTime +
                ", batchEndTime=" + batchEndTime +
                ", presentationDate=" + presentationDate +
                ", courseFees=" + courseFees +
                ", courseFeesFrom=" + courseFeesFrom +
                ", courseFeesTo=" + courseFeesTo +
                ", batchIsActive=" + batchIsActive +
                '}';
    }
}