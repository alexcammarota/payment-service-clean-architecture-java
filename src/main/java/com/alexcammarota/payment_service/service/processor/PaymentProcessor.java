package com.alexcammarota.payment_service.service.processor;

import com.alexcammarota.payment_service.model.Payment;
import com.alexcammarota.payment_service.model.PaymentMethod;

public interface PaymentProcessor {

    boolean supports(PaymentMethod method);

    void process(Payment payment);
}
