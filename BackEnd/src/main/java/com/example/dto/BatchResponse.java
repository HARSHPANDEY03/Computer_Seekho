package com.example.dto;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BatchResponse {
    private Integer batchId;
    private String batchName;
    private LocalTime batchStartTime;
    private LocalTime batchEndTime;
    private Integer courseId;
    private String courseName;
    private LocalDateTime presentationDate;
    private BigDecimal courseFees;
    private LocalDate courseFeesFrom;
    private LocalDate courseFeesTo;
    private Boolean batchIsActive;

    public Integer getBatchId() { return batchId; }
    public void setBatchId(Integer batchId) { this.batchId = batchId; }

    public String getBatchName() { return batchName; }
    public void setBatchName(String batchName) { this.batchName = batchName; }

    public LocalTime getBatchStartTime() { return batchStartTime; }
    public void setBatchStartTime(LocalTime batchStartTime) { this.batchStartTime = batchStartTime; }

    public LocalTime getBatchEndTime() { return batchEndTime; }
    public void setBatchEndTime(LocalTime batchEndTime) { this.batchEndTime = batchEndTime; }

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

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