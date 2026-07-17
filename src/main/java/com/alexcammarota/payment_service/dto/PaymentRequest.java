package com.alexcammarota.payment_service.dto;

import com.alexcammarota.payment_service.model.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest (
    @NotNull
    UUID customerId,

    @NotNull
    @DecimalMin(value = "0.01")
    BigDecimal amount,

    @NotBlank
    String currency,

    @NotNull
    PaymentMethod paymentMethod
){}
