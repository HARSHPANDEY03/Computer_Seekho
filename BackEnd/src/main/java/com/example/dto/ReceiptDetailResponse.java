package com.example.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Flat, cycle-free representation of a Receipt for the frontend receipt
 * page. The raw Receipt entity can't be serialized directly - Course has a
 * @OneToMany List<Batch>, and Batch has a @ManyToOne back to Course, and
 * neither side breaks the cycle for Jackson, so returning the entity
 * directly crashes with "Document nesting depth exceeds the maximum
 * allowed" once a Course with any Batches is involved.
 */
public class ReceiptDetailResponse {

    private Integer receiptId;
    private LocalDate receiptDate;
    private BigDecimal receiptAmount;
    private StudentInfo student;
    private PaymentInfo payment;
    private CourseInfo course;

    public Integer getReceiptId() { return receiptId; }
    public void setReceiptId(Integer receiptId) { this.receiptId = receiptId; }

    public LocalDate getReceiptDate() { return receiptDate; }
    public void setReceiptDate(LocalDate receiptDate) { this.receiptDate = receiptDate; }

    public BigDecimal getReceiptAmount() { return receiptAmount; }
    public void setReceiptAmount(BigDecimal receiptAmount) { this.receiptAmount = receiptAmount; }

    public StudentInfo getStudent() { return student; }
    public void setStudent(StudentInfo student) { this.student = student; }

    public PaymentInfo getPayment() { return payment; }
    public void setPayment(PaymentInfo payment) { this.payment = payment; }

    public CourseInfo getCourse() { return course; }
    public void setCourse(CourseInfo course) { this.course = course; }

    public static class StudentInfo {
        private Integer studentId;
        private String studentName;
        private String studentAddress;
        private String studentGender;
        private LocalDate studentDob;
        private Long studentMobile;
        private String studentEmail;

        public Integer getStudentId() { return studentId; }
        public void setStudentId(Integer studentId) { this.studentId = studentId; }
        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }
        public String getStudentAddress() { return studentAddress; }
        public void setStudentAddress(String studentAddress) { this.studentAddress = studentAddress; }
        public String getStudentGender() { return studentGender; }
        public void setStudentGender(String studentGender) { this.studentGender = studentGender; }
        public LocalDate getStudentDob() { return studentDob; }
        public void setStudentDob(LocalDate studentDob) { this.studentDob = studentDob; }
        public Long getStudentMobile() { return studentMobile; }
        public void setStudentMobile(Long studentMobile) { this.studentMobile = studentMobile; }
        public String getStudentEmail() { return studentEmail; }
        public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    }

    public static class CourseInfo {
        private Integer courseId;
        private String courseName;

        public Integer getCourseId() { return courseId; }
        public void setCourseId(Integer courseId) { this.courseId = courseId; }
        public String getCourseName() { return courseName; }
        public void setCourseName(String courseName) { this.courseName = courseName; }
    }

    public static class BatchInfo {
        private Integer batchId;
        private String batchName;

        public Integer getBatchId() { return batchId; }
        public void setBatchId(Integer batchId) { this.batchId = batchId; }
        public String getBatchName() { return batchName; }
        public void setBatchName(String batchName) { this.batchName = batchName; }
    }

    public static class PaymentTypeInfo {
        private String paymentTypeDesc;

        public String getPaymentTypeDesc() { return paymentTypeDesc; }
        public void setPaymentTypeDesc(String paymentTypeDesc) { this.paymentTypeDesc = paymentTypeDesc; }
    }

    public static class PaymentInfo {
        private Integer paymentId;
        private Double amount;
        private LocalDate paymentDate;
        private String status;
        private String razorpayPaymentId;
        private CourseInfo course;
        private BatchInfo batch;
        private PaymentTypeInfo paymentType;

        public Integer getPaymentId() { return paymentId; }
        public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }
        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
        public LocalDate getPaymentDate() { return paymentDate; }
        public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getRazorpayPaymentId() { return razorpayPaymentId; }
        public void setRazorpayPaymentId(String razorpayPaymentId) { this.razorpayPaymentId = razorpayPaymentId; }
        public CourseInfo getCourse() { return course; }
        public void setCourse(CourseInfo course) { this.course = course; }
        public BatchInfo getBatch() { return batch; }
        public void setBatch(BatchInfo batch) { this.batch = batch; }
        public PaymentTypeInfo getPaymentType() { return paymentType; }
        public void setPaymentType(PaymentTypeInfo paymentType) { this.paymentType = paymentType; }
    }
}