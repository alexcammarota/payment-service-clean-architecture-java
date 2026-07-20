package com.alexcammarota.payment_service.application.usecase;

import com.alexcammarota.payment_service.application.command.ProcessPaymentCommand;
import com.alexcammarota.payment_service.application.port.input.ProcessPayment;
import com.alexcammarota.payment_service.application.port.output.PaymentNotifier;
import com.alexcammarota.payment_service.application.port.output.PaymentRepository;
import com.alexcammarota.payment_service.application.processor.PaymentProcessorResolver;
import com.alexcammarota.payment_service.application.validation.PaymentValidator;
import com.alexcammarota.payment_service.domain.model.Payment;
import com.alexcammarota.payment_service.domain.model.PaymentStatus;

import java.time.Instant;
import java.util.UUID;

public class ProcessPaymentUseCase implements ProcessPayment {

    private final PaymentValidator paymentValidator;
    private final PaymentProcessorResolver paymentProcessorResolver;
    private final PaymentRepository paymentRepository;
    private final PaymentNotifier paymentNotifier;

    public ProcessPaymentUseCase(PaymentValidator paymentValidator,
                                 PaymentProcessorResolver paymentProcessorResolver,
                                 PaymentRepository paymentRepository,
                                 PaymentNotifier paymentNotifier) {
        this.paymentValidator = paymentValidator;
        this.paymentProcessorResolver = paymentProcessorResolver;
        this.paymentRepository = paymentRepository;
        this.paymentNotifier = paymentNotifier;
    }

    @Override
    public Payment execute(ProcessPaymentCommand command) {
        paymentValidator.validate(command);

        Payment payment = new Payment(
                UUID.randomUUID(),
                command.customerId(),
                command.amount(),
                command.currency(),
                command.paymentMethod(),
                PaymentStatus.PENDING,
                Instant.now()
        );

        paymentProcessorResolver.process(payment);
        paymentRepository.save(payment);
        paymentNotifier.send(payment);

        return payment;
    }
}
