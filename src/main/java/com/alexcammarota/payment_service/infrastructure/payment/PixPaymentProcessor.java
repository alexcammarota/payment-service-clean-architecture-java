package com.alexcammarota.payment_service.infrastructure.payment;

import com.alexcammarota.payment_service.application.port.output.PaymentProcessor;
import com.alexcammarota.payment_service.domain.model.Payment;
import com.alexcammarota.payment_service.domain.model.PaymentMethod;
import com.alexcammarota.payment_service.domain.model.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class PixPaymentProcessor implements PaymentProcessor {
    @Override
    public boolean supports(PaymentMethod method) {
        return method == PaymentMethod.PIX;
    }

    @Override
    public void process(Payment payment) {
        System.out.println("Processing PIX payment");
        payment.setStatus(PaymentStatus.APPROVED);
    }
}
