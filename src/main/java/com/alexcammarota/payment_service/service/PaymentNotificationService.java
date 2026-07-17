package com.alexcammarota.payment_service.service;

import com.alexcammarota.payment_service.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentNotificationService {

    public void send(Payment payment){
        System.out.println("Sending notification to the customer: " + payment.getCustomerId()
                + " | payment status: " + payment.getStatus());
    }
}
