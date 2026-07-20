package com.alexcammarota.payment_service.repository;


import com.alexcammarota.payment_service.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryPaymentRepository implements PaymentRepository{

    private final Map<UUID, Payment> payments = new ConcurrentHashMap<>();

    @Override
    public Payment save(Payment payment) {
        payments.put(payment.getId(), payment);
        return payment;
    }

    @Override
    public Optional<Payment> findById(UUID paymentId) {
        return Optional.ofNullable(payments.get(paymentId));
    }
}
