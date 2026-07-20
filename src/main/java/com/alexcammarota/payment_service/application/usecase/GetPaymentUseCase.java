package com.alexcammarota.payment_service.application.usecase;

import com.alexcammarota.payment_service.application.port.input.GetPayment;
import com.alexcammarota.payment_service.application.port.output.PaymentRepository;
import com.alexcammarota.payment_service.domain.model.Payment;

import java.util.UUID;

public class GetPaymentUseCase implements GetPayment {

    private final PaymentRepository paymentRepository;

    public GetPaymentUseCase(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment execute(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Payment not found")
                );
    }
}
