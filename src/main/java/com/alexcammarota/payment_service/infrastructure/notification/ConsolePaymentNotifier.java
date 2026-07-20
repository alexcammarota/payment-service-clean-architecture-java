package com.alexcammarota.payment_service.infrastructure.notification;

import com.alexcammarota.payment_service.application.port.output.PaymentNotifier;
import com.alexcammarota.payment_service.domain.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class ConsolePaymentNotifier implements PaymentNotifier {

    @Override
    public void send(Payment payment){
        System.out.println("Sending notification to the customer: " + payment.getCustomerId()
                + " | payment status: " + payment.getStatus());
    }
}
