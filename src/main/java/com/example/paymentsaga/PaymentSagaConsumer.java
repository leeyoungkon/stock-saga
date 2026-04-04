package com.example.paymentsaga;

import com.example.paymentsaga.SagaEvent;
import com.example.paymentsaga.PaymentProducer;
import com.example.paymentsaga.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentSagaConsumer {

    private final PaymentService paymentService;
    private final PaymentProducer paymentProducer;

    @KafkaListener(topics = "stock-events", groupId = "payment-service-group")
    public void consumeStockEvent(SagaEvent event) {
        log.info("[PAYMENT] stock event received = {}", event);

        if (!"StockReserved".equals(event.getEventType())) {
            return;
        }

        boolean ok = paymentService.pay(event.getOrderId(), event.getAmount());

        if (ok) {
            paymentProducer.publish(new SagaEvent(
                    "PaymentCompleted",
                    event.getOrderId(),
                    event.getProductId(),
                    event.getQuantity(),
                    event.getAmount(),
                    null
            ));
        } else {
            paymentProducer.publish(new SagaEvent(
                    "PaymentFailed",
                    event.getOrderId(),
                    event.getProductId(),
                    event.getQuantity(),
                    event.getAmount(),
                    "결제 승인 실패"
            ));
        }
    }
}