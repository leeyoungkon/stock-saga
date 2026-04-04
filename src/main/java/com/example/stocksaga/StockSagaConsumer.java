package com.example.stocksaga;


import com.example.stocksaga.SagaEvent;
import com.example.stocksaga.InventoryService;
import com.example.stocksaga.StockProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockSagaConsumer {

    private final InventoryService inventoryService;
    private final StockProducer stockProducer;
    private final SagaEventLogRepository sagaEventLogRepository;

    @KafkaListener(topics = "order-events", groupId = "stock-service-group")
    public void consumeOrderEvent(SagaEvent event) {
        log.info("[STOCK] order event received = {}", event);
        saveEvent(event);

        switch (event.getEventType()) {
            case "OrderCreated" -> {
                boolean ok = inventoryService.reserve(event.getProductId(), event.getQuantity());
                if (ok) {
                    SagaEvent result = new SagaEvent(
                            "StockReserved",
                            event.getOrderId(),
                            event.getProductId(),
                            event.getQuantity(),
                            event.getAmount(),
                            null
                    );
                    stockProducer.publish(result);
                    saveEvent(result);
                } else {
                    SagaEvent result = new SagaEvent(
                            "StockFailed",
                            event.getOrderId(),
                            event.getProductId(),
                            event.getQuantity(),
                            event.getAmount(),
                            "재고 부족"
                    );
                    stockProducer.publish(result);
                    saveEvent(result);
                }
            }
            case "OrderCancelled" -> {
                inventoryService.release(event.getProductId(), event.getQuantity());
                log.info("[STOCK] compensation completed for orderId={}", event.getOrderId());
                SagaEvent result = new SagaEvent(
                        "StockReleased",
                        event.getOrderId(),
                        event.getProductId(),
                        event.getQuantity(),
                        event.getAmount(),
                        null
                );
                saveEvent(result);
            }
        }
    }

    private void saveEvent(SagaEvent event) {
        sagaEventLogRepository.save(SagaEventLog.from(event));
    }
}