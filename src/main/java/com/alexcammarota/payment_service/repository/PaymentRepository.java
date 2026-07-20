package com.alexcammarota.payment_service.repository;

import com.alexcammarota.payment_service.model.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Payment save(Payment payment);
    Optional<Payment> findById(UUID paymentId);
}
