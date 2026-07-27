package com.example.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CourseRequest {
    private String courseCategory;
    private String courseName;
    private String courseDescription;
    private Integer courseDuration;
    private BigDecimal courseFees;
    private LocalDate courseFeesFrom;
    private LocalDate courseFeesTo;
    private String courseSyllabus;
    private String ageGrpType;
    private Boolean courseIsActive;
    private String coverPhoto;
    private Boolean isFeatured;

    public String getCourseCategory() { return courseCategory; }
    public void setCourseCategory(String courseCategory) { this.courseCategory = courseCategory; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCourseDescription() { return courseDescription; }
    public void setCourseDescription(String courseDescription) { this.courseDescription = courseDescription; }

    public Integer getCourseDuration() { return courseDuration; }
    public void setCourseDuration(Integer courseDuration) { this.courseDuration = courseDuration; }

    public BigDecimal getCourseFees() { return courseFees; }
    public void setCourseFees(BigDecimal courseFees) { this.courseFees = courseFees; }

    public LocalDate getCourseFeesFrom() { return courseFeesFrom; }
    public void setCourseFeesFrom(LocalDate courseFeesFrom) { this.courseFeesFrom = courseFeesFrom; }

    public LocalDate getCourseFeesTo() { return courseFeesTo; }
    public void setCourseFeesTo(LocalDate courseFeesTo) { this.courseFeesTo = courseFeesTo; }

    public String getCourseSyllabus() { return courseSyllabus; }
    public void setCourseSyllabus(String courseSyllabus) { this.courseSyllabus = courseSyllabus; }

    public String getAgeGrpType() { return ageGrpType; }
    public void setAgeGrpType(String ageGrpType) { this.ageGrpType = ageGrpType; }

    public Boolean getCourseIsActive() { return courseIsActive; }
    public void setCourseIsActive(Boolean courseIsActive) { this.courseIsActive = courseIsActive; }

    public String getCoverPhoto() { return coverPhoto; }
    public void setCoverPhoto(String coverPhoto) { this.coverPhoto = coverPhoto; }

    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }
}