package com.example.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BatchRequest {
    private String batchName;
    private LocalTime batchStartTime;
    private LocalTime batchEndTime;
    private Integer courseId;
    private LocalDateTime presentationDate;
    private BigDecimal courseFees;
    private LocalDate courseFeesFrom;
    private LocalDate courseFeesTo;
    private Boolean batchIsActive;

    public String getBatchName() { return batchName; }
    public void setBatchName(String batchName) { this.batchName = batchName; }

    public LocalTime getBatchStartTime() { return batchStartTime; }
    public void setBatchStartTime(LocalTime batchStartTime) { this.batchStartTime = batchStartTime; }

    public LocalTime getBatchEndTime() { return batchEndTime; }
    public void setBatchEndTime(LocalTime batchEndTime) { this.batchEndTime = batchEndTime; }

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public LocalDateTime getPresentationDate() { return presentationDate; }
    public void setPresentationDate(LocalDateTime presentationDate) { this.presentationDate = presentationDate; }

    public BigDecimal getCourseFees() { return courseFees; }
    public void setCourseFees(BigDecimal courseFees) { this.courseFees = courseFees; }

    public LocalDate getCourseFeesFrom() { return courseFeesFrom; }
    public void setCourseFeesFrom(LocalDate courseFeesFrom) { this.courseFeesFrom = courseFeesFrom; }

    public LocalDate getCourseFeesTo() { return courseFeesTo; }
    public void setCourseFeesTo(LocalDate courseFeesTo) { this.courseFeesTo = courseFeesTo; }

    public Boolean getBatchIsActive() { return batchIsActive; }
    public void setBatchIsActive(Boolean batchIsActive) { this.batchIsActive = batchIsActive; }
}