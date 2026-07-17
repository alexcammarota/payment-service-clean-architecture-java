package com.alexcammarota.payment_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;


@AllArgsConstructor
@Getter
public class Payment {

    private final UUID id;
    private final UUID customerId;
    private final BigDecimal amount;
    private final String currency;
    private final PaymentMethod paymentMethod;

    @Setter
    private PaymentStatus status;
    private final Instant createdAt;

}
