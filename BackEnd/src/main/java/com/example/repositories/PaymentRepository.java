package com.example.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entities.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByStudentStudentId(Integer studentId);

    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p")
    Double getTotalFeesReceived();

    /*
     * Approximation: total course fee owed by all students minus total
     * amount actually paid so far. There's no per-payment "due" tracking
     * in the schema yet, so this is a school-wide figure, not per student.
     */
    @Query("SELECT COALESCE((SELECT SUM(s.courseFee) FROM Student s), 0) - " +
            "COALESCE((SELECT SUM(p.amount) FROM Payment p), 0)")
    Double getPendingFees();

    // Sum of PAID payments for one student - used to compute their
    // remaining installment balance (courseFee - this).
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p " +
            "WHERE p.student.studentId = :studentId AND p.status = 'PAID'")
    Double sumPaidAmountByStudent(@Param("studentId") Integer studentId);
}
