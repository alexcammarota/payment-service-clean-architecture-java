package com.alexcammarota.payment_service.application.port.input;

import com.alexcammarota.payment_service.application.command.ProcessPaymentCommand;
import com.alexcammarota.payment_service.domain.model.Payment;

public interface ProcessPayment {
    Payment execute(ProcessPaymentCommand command);
}
