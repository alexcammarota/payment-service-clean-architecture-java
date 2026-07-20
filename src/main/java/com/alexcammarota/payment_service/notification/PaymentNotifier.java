package com.alexcammarota.payment_service.notification;

import com.alexcammarota.payment_service.model.Payment;

public interface PaymentNotifier {

    void send(Payment payment);
}
