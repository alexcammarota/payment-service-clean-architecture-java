package com.alexcammarota.payment_service.notification;

import com.alexcammarota.payment_service.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class ConsolePaymentNotifier implements PaymentNotifier{

    @Override
    public void send(Payment payment){
        System.out.println("Sending notification to the customer: " + payment.getCustomerId()
                + " | payment status: " + payment.getStatus());
    }
}
