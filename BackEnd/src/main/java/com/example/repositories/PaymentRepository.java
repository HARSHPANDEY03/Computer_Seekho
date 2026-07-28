package com.example.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entities.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByStudentStudentId(Integer studentId);

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
}