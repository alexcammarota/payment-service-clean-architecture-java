package com.alexcammarota.payment_service.service.processor;

import com.alexcammarota.payment_service.model.Payment;
import com.alexcammarota.payment_service.model.PaymentMethod;
import com.alexcammarota.payment_service.model.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class BankTransferPaymentProcessor implements PaymentProcessor{
    @Override
    public boolean supports(PaymentMethod method) {
        return method == PaymentMethod.BANK_TRANSFER;
    }

    @Override
    public void process(Payment payment) {
        System.out.println("Processing bank transfer payment");
        payment.setStatus(PaymentStatus.PENDING);
    }
}
