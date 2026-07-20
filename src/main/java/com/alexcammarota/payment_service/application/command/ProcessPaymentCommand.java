package com.alexcammarota.payment_service.application.command;

import com.alexcammarota.payment_service.domain.model.PaymentMethod;

import java.math.BigDecimal;
import java.util.UUID;

public record ProcessPaymentCommand(
        UUID customerId,
        BigDecimal amount,
        String currency,
        PaymentMethod paymentMethod
) {
}
