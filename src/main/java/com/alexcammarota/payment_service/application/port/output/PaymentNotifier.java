package com.alexcammarota.payment_service.application.port.output;

import com.alexcammarota.payment_service.domain.model.Payment;

public interface PaymentNotifier {

    void send(Payment payment);
}
