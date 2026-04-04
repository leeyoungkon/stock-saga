package com.example.stocksaga;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StockController {

    private final InventoryService inventoryService;
    private final SagaEventLogRepository sagaEventLogRepository;

    @GetMapping("/stocks")
    public Map<String, Integer> current() {
        return inventoryService.currentStock();
    }

    @GetMapping("/inventory")
    public List<InventoryItem> inventory() {
        return inventoryService.currentStockItems();
    }

    @PostMapping("/inventory/{productId}")
    public InventoryItem upsert(@PathVariable String productId, @RequestBody InventoryUpdateRequest request) {
        if (request.quantity() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "quantity must be >= 0");
        }
        return inventoryService.upsert(productId, request.quantity());
    }

    @GetMapping("/events")
    public List<SagaEventLog> events() {
        return sagaEventLogRepository.findTop200ByOrderByCreatedAtDesc();
    }

    public record InventoryUpdateRequest(int quantity) {
    }
}