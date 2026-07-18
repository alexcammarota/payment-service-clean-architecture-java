package com.alexcammarota.payment_service.service.processor;

import com.alexcammarota.payment_service.model.PaymentMethod;
import com.alexcammarota.payment_service.model.PaymentStatus;

class CreditCardPaymentProcessorTest
        extends PaymentProcessorContractTest {

    @Override
    protected PaymentProcessor processor() {
        return new CreditCardPaymentProcessor();
    }

    @Override
    protected PaymentMethod supportedMethod() {
        return PaymentMethod.CREDIT_CARD;
    }

    @Override
    protected PaymentStatus expectedStatus() {
        return PaymentStatus.APPROVED;
    }
}