package com.example.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "payment", uniqueConstraints = {
        @UniqueConstraint(columnNames = "razorpay_order_id")
})
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_typeID")
    private PaymentType paymentType;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @Column(name = "amount")
    private Double amount;

    // --- Razorpay integration fields ---

    @Column(name = "razorpay_order_id", length = 64, unique = true)
    private String razorpayOrderId;

    @Column(name = "razorpay_payment_id", length = 64)
    private String razorpayPaymentId;

    /**
     * CREATED  - order created with Razorpay, checkout not completed yet.
     * PAID     - signature verified, student/receipt persisted.
     * FAILED   - signature verification failed.
     * Only relevant for online (Razorpay) payments; manually recorded
     * offline payments (cash/cheque) can leave this null.
     */
    @Column(name = "status", length = 20)
    private String status;

    // Username of the logged-in staff member who recorded/verified this
    // payment, taken from the JWT at the time it happened. Null for
    // payments recorded before this field existed, and for CREATED/FAILED
    // rows where nothing was actually collected.
    @Column(name = "collected_by", length = 100)
    private String collectedBy;

    // Optional free-text note the admin can attach to a payment
    // (e.g. "partial payment, balance due next week").
    @Column(name = "remarks", length = 500)
    private String remarks;

    // Default Constructor
    public Payment() {
    }

    // Parameterized Constructor
    public Payment(Integer paymentId, PaymentType paymentType, LocalDate paymentDate,
                   Student student, Course course, Batch batch, Double amount) {
        this.paymentId = paymentId;
        this.paymentType = paymentType;
        this.paymentDate = paymentDate;
        this.student = student;
        this.course = course;
        this.batch = batch;
        this.amount = amount;
    }

    // Getters and Setters

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCollectedBy() {
        return collectedBy;
    }

    public void setCollectedBy(String collectedBy) {
        this.collectedBy = collectedBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", paymentDate=" + paymentDate +
                ", amount=" + amount +
                '}';
    }
}