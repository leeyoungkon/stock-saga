package com.example.stocksaga;

import com.example.stocksaga.SagaEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockProducer {

    private final KafkaTemplate<String, SagaEvent> kafkaTemplate;

    public void publish(SagaEvent event) {
        kafkaTemplate.send("stock-events", event.getOrderId(), event);
    }
}