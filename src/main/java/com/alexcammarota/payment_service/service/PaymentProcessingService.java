package com.alexcammarota.payment_service.service;

import com.alexcammarota.payment_service.model.Payment;
import com.alexcammarota.payment_service.service.processor.PaymentProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentProcessingService {

    private final List<PaymentProcessor> paymentProcessors;

    public PaymentProcessingService(List<PaymentProcessor> paymentProcessors){
        this.paymentProcessors = paymentProcessors;
    }

    public void process(Payment payment){
        PaymentProcessor paymentProcessor = paymentProcessors.stream()
                .filter(candidate -> candidate.supports(payment.getPaymentMethod()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported Payment Method: " + payment.getPaymentMethod()));

        paymentProcessor.process(payment);
    }
}
