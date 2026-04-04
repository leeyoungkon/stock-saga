package com.example.paymentsaga;

import com.example.paymentsaga.SagaEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProducer {

    private final KafkaTemplate<String, SagaEvent> kafkaTemplate;

    public void publish(SagaEvent event) {
        kafkaTemplate.send("payment-events", event.getOrderId(), event);
    }
}