package com.alexcammarota.payment_service.application.port.input;

import com.alexcammarota.payment_service.domain.model.Payment;

import java.util.UUID;

public interface GetPayment {

    Payment execute(UUID paymentId);
}
