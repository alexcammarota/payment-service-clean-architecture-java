package com.alexcammarota.payment_service.infrastructure.payment;

import com.alexcammarota.payment_service.application.port.output.PaymentProcessor;
import com.alexcammarota.payment_service.domain.model.Payment;
import com.alexcammarota.payment_service.domain.model.PaymentMethod;
import com.alexcammarota.payment_service.domain.model.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class BankTransferPaymentProcessor implements PaymentProcessor {
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
