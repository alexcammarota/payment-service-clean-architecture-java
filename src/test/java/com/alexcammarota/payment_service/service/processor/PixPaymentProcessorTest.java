package com.alexcammarota.payment_service.service.processor;

import com.alexcammarota.payment_service.model.PaymentMethod;
import com.alexcammarota.payment_service.model.PaymentStatus;

class PixPaymentProcessorTest
        extends PaymentProcessorContractTest {

    @Override
    protected PaymentProcessor processor() {
        return new PixPaymentProcessor();
    }

    @Override
    protected PaymentMethod supportedMethod() {
        return PaymentMethod.PIX;
    }

    @Override
    protected PaymentStatus expectedStatus() {
        return PaymentStatus.APPROVED;
    }
}