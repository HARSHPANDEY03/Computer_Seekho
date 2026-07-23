package com.example.entities;

import java.math.BigDecimal;

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
@Table(name = "placed_student")
public class PlacedStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "placed_student_id", nullable = false)
    private Integer placedStudentId;

    @Column(name = "placed_student_name", length = 100)
    private String placedStudentName;

    @Column(
        name = "placement_package",
        precision = 10,
        scale = 2
    )
    private BigDecimal placementPackage;

    /*
     * Many placed students can be associated
     * with the same recruiter.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id")
    @JsonIgnoreProperties({
        "placedStudents",
        "hibernateLazyInitializer",
        "handler"
    })
    private Recruiter recruiter;

    /*
     * Many placed students can belong
     * to the same batch.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    @JsonIgnoreProperties({
        "placedStudents",
        "students",
        "course",
        "hibernateLazyInitializer",
        "handler"
    })
    private Batch batch;

    public PlacedStudent() {
    }

    public PlacedStudent(
            String placedStudentName,
            BigDecimal placementPackage,
            Recruiter recruiter,
            Batch batch) {

        this.placedStudentName = placedStudentName;
        this.placementPackage = placementPackage;
        this.recruiter = recruiter;
        this.batch = batch;
    }

    public Integer getPlacedStudentId() {
        return placedStudentId;
    }

    public void setPlacedStudentId(Integer placedStudentId) {
        this.placedStudentId = placedStudentId;
    }

    public String getPlacedStudentName() {
        return placedStudentName;
    }

    public void setPlacedStudentName(String placedStudentName) {
        this.placedStudentName = placedStudentName;
    }

    public BigDecimal getPlacementPackage() {
        return placementPackage;
    }

    public void setPlacementPackage(BigDecimal placementPackage) {
        this.placementPackage = placementPackage;
    }

    public Recruiter getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(Recruiter recruiter) {
        this.recruiter = recruiter;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    @Override
    public String toString() {
        return "PlacedStudent{" +
                "placedStudentId=" + placedStudentId +
                ", placedStudentName='" + placedStudentName + '\'' +
                ", placementPackage=" + placementPackage +
                '}';
    }
}