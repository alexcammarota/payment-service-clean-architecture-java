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

    private final PaymentValidator paymentValidator;
    private final PaymentProcessingService paymentProcessingService;
    private final PaymentRepository paymentRepository;
    private final PaymentNotificationService notificationService;

    public PaymentService(PaymentValidator paymentValidator,
                          PaymentProcessingService paymentProcessingService,
                          PaymentRepository paymentRepository,
                          PaymentNotificationService notificationService) {
        this.paymentValidator = paymentValidator;
        this.paymentProcessingService = paymentProcessingService;
        this.paymentRepository = paymentRepository;
        this.notificationService = notificationService;
    }

    public Payment process(PaymentRequest request){
        paymentValidator.validate(request);


        Payment payment = new Payment(
                UUID.randomUUID(),
                request.customerId(),
                request.amount(),
                request.currency(),
                request.paymentMethod(),
                PaymentStatus.PENDING,
                Instant.now()
        );

        paymentProcessingService.process(payment);

        paymentRepository.save(payment);

        notificationService.send(payment);

        return payment;
    }


    public Payment findById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Payment not found")
                );
    }
}
