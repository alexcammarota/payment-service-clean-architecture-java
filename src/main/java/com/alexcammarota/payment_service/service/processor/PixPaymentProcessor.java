package com.alexcammarota.payment_service.service.processor;

import com.alexcammarota.payment_service.model.Payment;
import com.alexcammarota.payment_service.model.PaymentMethod;
import com.alexcammarota.payment_service.model.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class PixPaymentProcessor implements PaymentProcessor{
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
