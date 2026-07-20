package com.alexcammarota.payment_service.service.processor;

import com.alexcammarota.payment_service.application.port.output.PaymentProcessor;
import com.alexcammarota.payment_service.domain.model.PaymentMethod;
import com.alexcammarota.payment_service.domain.model.PaymentStatus;
import com.alexcammarota.payment_service.infrastructure.payment.BankTransferPaymentProcessor;

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