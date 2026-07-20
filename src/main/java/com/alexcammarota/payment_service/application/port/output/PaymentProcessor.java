package com.alexcammarota.payment_service.application.port.output;

import com.alexcammarota.payment_service.domain.model.Payment;
import com.alexcammarota.payment_service.domain.model.PaymentMethod;

public interface PaymentProcessor {

    boolean supports(PaymentMethod method);

    void process(Payment payment);
}
