package com.alexcammarota.payment_service.service;

import com.alexcammarota.payment_service.dto.PaymentRequest;
import org.springframework.stereotype.Component;

@Component
public class PaymentValidator {

    public void validate(PaymentRequest request) {
        if (request.customerId() == null) {
            throw new IllegalArgumentException("Customer ID must be informed");
        }

        if (request.amount() == null || request.amount().signum() <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }

        if (request.currency() == null || request.currency().isBlank()) {
            throw new IllegalArgumentException("Currency must be informed");
        }

        if (request.paymentMethod() == null) {
            throw new IllegalArgumentException(
                    "Payment method must be informed"
            );
        }
    }
}
