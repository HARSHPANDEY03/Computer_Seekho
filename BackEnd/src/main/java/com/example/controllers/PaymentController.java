package com.example.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.CreateOrderRequest;
import com.example.dto.CreateOrderResponse;
import com.example.dto.PaymentHistoryItemResponse;
import com.example.dto.PaymentReceiptResponse;
import com.example.dto.PaymentSummaryResponse;
import com.example.dto.VerifyPaymentRequest;
import com.example.services.RazorpayPaymentService;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final RazorpayPaymentService paymentService;

    public PaymentController(RazorpayPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Step 1: called when the admin submits a payment (first payment or a
    // follow-up installment - see CreateOrderRequest.studentId). Creates a
    // Razorpay order for a DB-verified amount.
    @PostMapping("/create-order")
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest request)
            throws RazorpayException {
        return ResponseEntity.ok(paymentService.createOrder(request));
    }

    // Step 2: called by the frontend's Razorpay "handler" callback after
    // checkout succeeds. Verifies the signature and persists everything.
    @PostMapping("/verify")
    public ResponseEntity<PaymentReceiptResponse> verify(@RequestBody VerifyPaymentRequest request) {
        return ResponseEntity.ok(paymentService.verifyAndPersist(request));
    }

    // Cash / Bank payment - no gateway step involved. Works for both the
    // first (admitting) payment and any later installment - see
    // VerifyPaymentRequest.studentId.
    @PostMapping("/offline")
    public ResponseEntity<PaymentReceiptResponse> offline(@RequestBody VerifyPaymentRequest request) {
        return ResponseEntity.ok(paymentService.recordOfflinePayment(request));
    }

    // Live course-fee / paid / pending balance for one student - used to
    // drive the installment UI (how much is left to collect).
    @GetMapping("/summary/{studentId}")
    public ResponseEntity<PaymentSummaryResponse> summary(@PathVariable Integer studentId) {
        return ResponseEntity.ok(paymentService.getPaymentSummary(studentId));
    }

    // Full payment/installment history for one student, oldest first,
    // including Pending/Failed attempts and each row's receipt number and
    // running remaining balance - powers the History tab.
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<PaymentHistoryItemResponse>> paymentHistory(@PathVariable Integer studentId) {
        return ResponseEntity.ok(paymentService.getPaymentHistory(studentId));
    }
}