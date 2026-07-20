package com.alexcammarota.payment_service.infrastructure.payment;

import com.alexcammarota.payment_service.application.port.output.PaymentProcessor;
import com.alexcammarota.payment_service.domain.model.Payment;
import com.alexcammarota.payment_service.domain.model.PaymentMethod;
import com.alexcammarota.payment_service.domain.model.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class CreditCardPaymentProcessor implements PaymentProcessor {
    @Override
    public boolean supports(PaymentMethod method) {
        return method == PaymentMethod.CREDIT_CARD;
    }

    @Override
    public void process(Payment payment) {
        System.out.println("Processing credit card payment");
        payment.setStatus(PaymentStatus.APPROVED);
    }
}
