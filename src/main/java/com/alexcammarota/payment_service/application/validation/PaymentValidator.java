package com.alexcammarota.payment_service.application.validation;

import com.alexcammarota.payment_service.application.command.ProcessPaymentCommand;

public class PaymentValidator {

    public void validate(ProcessPaymentCommand command) {
        if (command.customerId() == null) {
            throw new IllegalArgumentException("Customer ID must be informed");
        }

        if (command.amount() == null || command.amount().signum() <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }

        if (command.currency() == null || command.currency().isBlank()) {
            throw new IllegalArgumentException("Currency must be informed");
        }

        if (command.paymentMethod() == null) {
            throw new IllegalArgumentException(
                    "Payment method must be informed"
            );
        }
    }
}
