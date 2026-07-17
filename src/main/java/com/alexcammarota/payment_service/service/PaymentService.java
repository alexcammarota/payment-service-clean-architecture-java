package com.alexcammarota.payment_service.service;

import com.alexcammarota.payment_service.dto.PaymentRequest;
import com.alexcammarota.payment_service.model.Payment;
import com.alexcammarota.payment_service.model.PaymentStatus;
import com.alexcammarota.payment_service.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment process(PaymentRequest request){
        if(request.amount() == null || request.amount().signum() <= 0){
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }


        Payment payment = new Payment(
                UUID.randomUUID(),
                request.customerId(),
                request.amount(),
                request.currency(),
                request.paymentMethod(),
                PaymentStatus.PENDING,
                Instant.now()
        );

        switch (request.paymentMethod()) {
            case PIX -> {
                System.out.println("Processing PIX payment");
                payment.setStatus(PaymentStatus.APPROVED);
            }

            case CREDIT_CARD -> {
                System.out.println("Processing credit card payment");
                payment.setStatus(PaymentStatus.APPROVED);
            }

            case BANK_TRANSFER -> {
                System.out.println("Processing bank transfer payment");

                payment.setStatus(PaymentStatus.PENDING);
            }
        }

        Payment paymentResponse = paymentRepository.save(payment);

        System.out.println(
                "Sending notification to customer: "
                        + payment.getCustomerId()
        );

        return paymentResponse;
    }


    public Payment findById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Payment not found")
                );
    }
}
