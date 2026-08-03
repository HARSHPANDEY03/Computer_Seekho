package com.example.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.entities.Batch;
import com.example.entities.Course;
import com.example.entities.Enquiry;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "student")
public class Student {

    /*
     * student_id
     * INT
     * Primary Key
     * Auto Increment
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id", nullable = false)
    private Integer studentId;

    /*
     * enquiry_id
     * Foreign Key
     *
     * One enquiry can create only one student.
     * unique = true prevents the same enquiry from being
     * assigned to another student.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enquiry_id", unique = true)
    @JsonIgnoreProperties({
        "student",
        "hibernateLazyInitializer",
        "handler"
    })
    private Enquiry enquiry;

    /*
     * student_name
     * VARCHAR(100)
     */
    @Column(name = "student_name", length = 100)
    private String studentName;

    /*
     * student_address
     * VARCHAR(255)
     */
    @Column(name = "student_address", length = 255)
    private String studentAddress;

    /*
     * student_gender
     * VARCHAR(10)
     */
    @Column(name = "student_gender", length = 10)
    private String studentGender;

    /*
     * photo_url
     * VARCHAR(255)
     */
    @Column(name = "photo_url", length = 255)
    private String photoUrl;

    /*
     * student_dob
     * DATE
     */
    @Column(name = "student_dob")
    private LocalDate studentDob;

    /*
     * student_qualification
     * VARCHAR(50)
     */
    @Column(name = "student_qualification", length = 50)
    private String studentQualification;

    /*
     * student_mobile
     * BIGINT
     */
    @Column(name = "student_mobile")
    private Long studentMobile;

    /*
     * course_fee
     * DECIMAL(10,2)
     */
    @Column(name = "course_fee", precision = 10, scale = 2)
    private BigDecimal courseFee;

    /*
     * batch_id
     * Foreign Key
     *
     * Many students can belong to the same batch.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    @JsonIgnoreProperties({
        "students",
        "course",
        "hibernateLazyInitializer",
        "handler"
    })
    private Batch batch;

    /*
     * course_id
     * Foreign Key
     *
     * Many students can register for the same course.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonIgnoreProperties({
        "students",
        "batches",
        "hibernateLazyInitializer",
        "handler"
    })
    private Course course;

    /*
     * student_email
     * VARCHAR(150)
     *
     * Used to send the admission-confirmation email (receipt + admission
     * slip). Optional - some walk-in admissions may not have one.
     */
    @Column(name = "student_email", length = 150)
    private String studentEmail;

    /*
     * student_username
     * VARCHAR(100)
     * Unique Key
     */
    @Column(
        name = "student_username",
        length = 100,
        unique = true
    )
    private String studentUsername;

    /*
     * student_password
     * VARCHAR(255)
     *
     * @JsonIgnore prevents the password from appearing
     * in API responses.
     */
    @JsonIgnore
    @Column(name = "student_password", length = 255)
    private String studentPassword;

    /*
     * JPA requires a no-argument constructor.
     */
    public Student() {
    }

    /*
     * Optional constructor without studentId.
     * studentId is generated automatically by MySQL.
     */
    public Student(
            Enquiry enquiry,
            String studentName,
            String studentAddress,
            String studentGender,
            String photoUrl,
            LocalDate studentDob,
            String studentQualification,
            Long studentMobile,
            BigDecimal courseFee,
            Batch batch,
            Course course,
            String studentUsername,
            String studentPassword) {

        this.enquiry = enquiry;
        this.studentName = studentName;
        this.studentAddress = studentAddress;
        this.studentGender = studentGender;
        this.photoUrl = photoUrl;
        this.studentDob = studentDob;
        this.studentQualification = studentQualification;
        this.studentMobile = studentMobile;
        this.courseFee = courseFee;
        this.batch = batch;
        this.course = course;
        this.studentUsername = studentUsername;
        this.studentPassword = studentPassword;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Enquiry getEnquiry() {
        return enquiry;
    }

    public void setEnquiry(Enquiry enquiry) {
        this.enquiry = enquiry;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentAddress() {
        return studentAddress;
    }

    public void setStudentAddress(String studentAddress) {
        this.studentAddress = studentAddress;
    }

    public String getStudentGender() {
        return studentGender;
    }

    public void setStudentGender(String studentGender) {
        this.studentGender = studentGender;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public LocalDate getStudentDob() {
        return studentDob;
    }

    public void setStudentDob(LocalDate studentDob) {
        this.studentDob = studentDob;
    }

    public String getStudentQualification() {
        return studentQualification;
    }

    public void setStudentQualification(String studentQualification) {
        this.studentQualification = studentQualification;
    }

    public Long getStudentMobile() {
        return studentMobile;
    }

    public void setStudentMobile(Long studentMobile) {
        this.studentMobile = studentMobile;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public BigDecimal getCourseFee() {
        return courseFee;
    }

    public void setCourseFee(BigDecimal courseFee) {
        this.courseFee = courseFee;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getStudentPassword() {
        return studentPassword;
    }

    public void setStudentPassword(String studentPassword) {
        this.studentPassword = studentPassword;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", studentAddress='" + studentAddress + '\'' +
                ", studentGender='" + studentGender + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", studentDob=" + studentDob +
                ", studentQualification='" + studentQualification + '\'' +
                ", studentMobile=" + studentMobile +
                ", courseFee=" + courseFee +
                ", studentUsername='" + studentUsername + '\'' +
                '}';
    }
}