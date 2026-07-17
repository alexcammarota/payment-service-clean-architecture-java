package com.alexcammarota.payment_service.service;

import com.alexcammarota.payment_service.model.Payment;
import com.alexcammarota.payment_service.model.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class PaymentProcessingService {


    public void process(Payment payment){
        switch (payment.getPaymentMethod()){
            case PIX -> processPix(payment);
            case CREDIT_CARD -> processCreditCard(payment);
            case BANK_TRANSFER -> processBankTransfer(payment);
        }
    }

    private void processBankTransfer(Payment payment) {
        System.out.println("Processing PIX payment");
        payment.setStatus(PaymentStatus.APPROVED);
    }

    private void processCreditCard(Payment payment) {
        System.out.println("Processing credit card payment");
        payment.setStatus(PaymentStatus.APPROVED);
        
    }

    private void processPix(Payment payment) {
        System.out.println("Processing bank transfer payment");
        payment.setStatus(PaymentStatus.PENDING);
    }
}
