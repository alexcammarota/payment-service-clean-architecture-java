package com.alexcammarota.payment_service.configuration;

import com.alexcammarota.payment_service.application.port.input.GetPayment;
import com.alexcammarota.payment_service.application.port.input.ProcessPayment;
import com.alexcammarota.payment_service.application.port.output.PaymentNotifier;
import com.alexcammarota.payment_service.application.port.output.PaymentProcessor;
import com.alexcammarota.payment_service.application.port.output.PaymentRepository;
import com.alexcammarota.payment_service.application.processor.PaymentProcessorResolver;
import com.alexcammarota.payment_service.application.usecase.GetPaymentUseCase;
import com.alexcammarota.payment_service.application.usecase.ProcessPaymentUseCase;
import com.alexcammarota.payment_service.application.validation.PaymentValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
public class BeanConfiguration {

    @Bean
    PaymentValidator paymentValidator(){
        return new PaymentValidator();
    }

    @Bean
    PaymentProcessorResolver paymentProcessorResolver(List<PaymentProcessor> processors){
        return new PaymentProcessorResolver(processors);
    }

    @Bean
    ProcessPayment processPayment(
            PaymentValidator paymentValidator,
            PaymentProcessorResolver paymentProcessorResolver,
            PaymentRepository paymentRepository,
            PaymentNotifier paymentNotifier
    ){
        return new ProcessPaymentUseCase(
                paymentValidator,
                paymentProcessorResolver,
                paymentRepository,
                paymentNotifier
        );
    }

    @Bean
    GetPayment getPayment(PaymentRepository paymentRepository){
        return new GetPaymentUseCase(paymentRepository);
    }

}
