package com.example.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.entities.PaymentType;
import com.example.repositories.PaymentTypeRepository;
import com.example.services.RazorpayPaymentService;

/**
 * Runs once on startup and makes sure the payment_type row the Razorpay
 * flow depends on ("Online (Razorpay)") exists, without touching any other
 * data. Safe to run on every boot - it only inserts when the row is absent.
 */
@Component
public class PaymentTypeDataInitializer implements CommandLineRunner {

    private final PaymentTypeRepository paymentTypeRepository;

    public PaymentTypeDataInitializer(PaymentTypeRepository paymentTypeRepository) {
        this.paymentTypeRepository = paymentTypeRepository;
    }

    @Override
    public void run(String... args) {
        if (paymentTypeRepository.findByPaymentTypeDescIgnoreCase(RazorpayPaymentService.ONLINE_PAYMENT_TYPE_DESC)
                .isEmpty()) {
            PaymentType type = new PaymentType();
            type.setPaymentTypeDesc(RazorpayPaymentService.ONLINE_PAYMENT_TYPE_DESC);
            paymentTypeRepository.save(type);
        }
    }
}
