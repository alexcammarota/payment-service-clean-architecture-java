package com.alexcammarota.payment_service.service.processor;

import com.alexcammarota.payment_service.model.Payment;
import com.alexcammarota.payment_service.model.PaymentMethod;
import com.alexcammarota.payment_service.model.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class CreditCardPaymentProcessor implements PaymentProcessor{
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
