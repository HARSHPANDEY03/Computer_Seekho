package com.example.services;

import java.util.List;

import com.example.dto.PaymentRequestDTO;
import com.example.dto.PaymentResponseDTO;


public interface PaymentService {
    PaymentResponseDTO createPayment(PaymentRequestDTO requestDTO);
    PaymentResponseDTO updatePayment(Integer paymentId, PaymentRequestDTO requestDTO);
    PaymentResponseDTO getPaymentById(Integer paymentId);
    List<PaymentResponseDTO> getAllPayments();
    void deletePayment(Integer paymentId);
    Double getTotalFeesReceived();
    Double getPendingFees();
    List<PaymentResponseDTO> getPaymentsByStudent(Integer studentId);
}