package com.alexcammarota.payment_service.application.port.output;

import com.alexcammarota.payment_service.domain.model.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    void save(Payment payment);
    Optional<Payment> findById(UUID paymentId);
}
