package com.alexcammarota.payment_service.service.processor;

import com.alexcammarota.payment_service.application.port.output.PaymentProcessor;
import com.alexcammarota.payment_service.domain.model.PaymentMethod;
import com.alexcammarota.payment_service.domain.model.PaymentStatus;
import com.alexcammarota.payment_service.infrastructure.payment.PixPaymentProcessor;

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