package com.example.repositories;
import com.example.entities.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {

    /**
     * Fetches a receipt by id together with everything needed to print it
     * (payment, payment type, student, course, batch) in a single query,
     * so the service layer does not trigger lazy-loading round trips.
     */
    @Query("SELECT r FROM Receipt r " +
            "LEFT JOIN FETCH r.payment p " +
            "LEFT JOIN FETCH p.paymentType " +
            "LEFT JOIN FETCH p.batch " +
            "LEFT JOIN FETCH r.student " +
            "LEFT JOIN FETCH r.course " +
            "WHERE r.receiptId = :receiptId")
    Optional<Receipt> findReceiptWithDetailsById(@Param("receiptId") Integer receiptId);

    /**
     * Fetches every receipt belonging to students whose name contains the
     * given text (case-insensitive), together with everything needed to
     * print it, in a single query.
     */
    @Query("SELECT r FROM Receipt r " +
            "LEFT JOIN FETCH r.payment p " +
            "LEFT JOIN FETCH p.paymentType " +
            "LEFT JOIN FETCH p.batch " +
            "LEFT JOIN FETCH r.student s " +
            "LEFT JOIN FETCH r.course " +
            "WHERE LOWER(s.studentName) LIKE LOWER(CONCAT('%', :studentName, '%'))")
    List<Receipt> findReceiptsWithDetailsByStudentName(@Param("studentName") String studentName);

    /**
     * Used by the Razorpay verification flow to idempotently return the
     * existing receipt when a browser retries a /verify call for an order
     * that has already been marked PAID.
     */
    Optional<Receipt> findByPayment_PaymentId(Integer paymentId);
}
