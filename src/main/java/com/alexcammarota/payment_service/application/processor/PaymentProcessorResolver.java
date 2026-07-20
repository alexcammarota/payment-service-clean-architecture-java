package com.alexcammarota.payment_service.application.processor;

import com.alexcammarota.payment_service.domain.model.Payment;
import com.alexcammarota.payment_service.application.port.output.PaymentProcessor;

import java.util.List;

public class PaymentProcessorResolver {

    private final List<PaymentProcessor> paymentProcessors;

    public PaymentProcessorResolver(List<PaymentProcessor> paymentProcessors){
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
