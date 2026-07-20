package com.alexcammarota.payment_service.presentation.rest;

import com.alexcammarota.payment_service.application.port.input.GetPayment;
import com.alexcammarota.payment_service.application.port.input.ProcessPayment;
import com.alexcammarota.payment_service.presentation.rest.request.PaymentRequest;
import com.alexcammarota.payment_service.presentation.rest.response.PaymentResponse;
import com.alexcammarota.payment_service.domain.model.Payment;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {

    private final ProcessPayment processPayment;
    private final GetPayment getPayment;

    public PaymentController(ProcessPayment processPayment, GetPayment getPayment) {
        this.processPayment = processPayment;
        this.getPayment = getPayment;
    }


    @PostMapping
    public ResponseEntity<PaymentResponse> process(@Valid @RequestBody PaymentRequest request) {
        Payment process = processPayment.execute(request.toCommand());
        PaymentResponse paymentResponse = PaymentResponse.from(process);

        return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse);
    }


    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> findById(@PathVariable UUID paymentId){
        Payment payment = getPayment.execute(paymentId);
        PaymentResponse paymentResponse = PaymentResponse.from(payment);
        return ResponseEntity.ok(paymentResponse);
    }




}
