package com.alexcammarota.payment_service.service.processor;

import com.alexcammarota.payment_service.model.Payment;
import com.alexcammarota.payment_service.model.PaymentMethod;
import com.alexcammarota.payment_service.model.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

abstract class PaymentProcessorContractTest {

    protected abstract PaymentProcessor processor();

    protected abstract PaymentMethod supportedMethod();

    protected abstract PaymentStatus expectedStatus();

    @Test
    void shouldSupportItsDeclaredPaymentMethod() {
        assertTrue(
                processor().supports(supportedMethod())
        );
    }

    @Test
    void shouldNotSupportOtherPaymentMethods() {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method != supportedMethod()) {
                assertFalse(
                        processor().supports(method)
                );
            }
        }
    }

    @Test
    void shouldProcessPaymentWithoutChangingItsIdentity() {
        UUID paymentId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("150.00");

        Payment payment = new Payment(
                paymentId,
                customerId,
                amount,
                "BRL",
                supportedMethod(),
                PaymentStatus.PENDING,
                Instant.now()
        );

        processor().process(payment);

        assertEquals(expectedStatus(), payment.getStatus());
        assertEquals(paymentId, payment.getId());
        assertEquals(customerId, payment.getCustomerId());
        assertEquals(amount, payment.getAmount());
        assertEquals("BRL", payment.getCurrency());
        assertEquals(supportedMethod(), payment.getPaymentMethod());
    }
}