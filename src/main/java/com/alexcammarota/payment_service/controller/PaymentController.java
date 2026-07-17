package com.alexcammarota.payment_service.controller;

import com.alexcammarota.payment_service.dto.PaymentRequest;
import com.alexcammarota.payment_service.dto.PaymentResponse;
import com.alexcammarota.payment_service.model.Payment;
import com.alexcammarota.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> process(@Valid @RequestBody PaymentRequest request) {
        Payment process = paymentService.process(request);
        PaymentResponse paymentResponse = PaymentResponse.from(process);

        return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse);
    }


    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> findById(@PathVariable UUID paymentId){
        Payment payment = paymentService.findById(paymentId);
        PaymentResponse paymentResponse = PaymentResponse.from(payment);
        return ResponseEntity.ok(paymentResponse);
    }




}
