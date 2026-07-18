package com.alexcammarota.payment_service.service.processor;

import com.alexcammarota.payment_service.model.PaymentMethod;
import com.alexcammarota.payment_service.model.PaymentStatus;

class BankTransferPaymentProcessorTest
        extends PaymentProcessorContractTest {

    @Override
    protected PaymentProcessor processor() {
        return new BankTransferPaymentProcessor();
    }

    @Override
    protected PaymentMethod supportedMethod() {
        return PaymentMethod.BANK_TRANSFER;
    }

    @Override
    protected PaymentStatus expectedStatus() {
        return PaymentStatus.PENDING;
    }
}