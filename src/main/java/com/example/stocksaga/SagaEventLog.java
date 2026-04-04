package com.example.stocksaga;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "saga_event_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SagaEventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "order_id", length = 100)
    private String orderId;

    @Column(name = "product_id", length = 100)
    private String productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "reason", length = 300)
    private String reason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public static SagaEventLog from(SagaEvent event) {
        SagaEventLog log = new SagaEventLog();
        log.setEventType(event.getEventType());
        log.setOrderId(event.getOrderId());
        log.setProductId(event.getProductId());
        log.setQuantity(event.getQuantity());
        log.setAmount(event.getAmount());
        log.setReason(event.getReason());
        log.setCreatedAt(LocalDateTime.now());
        return log;
    }
}
