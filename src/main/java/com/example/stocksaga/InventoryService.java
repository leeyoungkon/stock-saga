package com.example.stocksaga;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @PostConstruct
    public void init() {
        initializeIfMissing("P100", 10);
        initializeIfMissing("P200", 1);
    }

    @Transactional
    public boolean reserve(String productId, int quantity) {
        InventoryItem item = inventoryRepository.findById(productId)
                .orElse(new InventoryItem(productId, 0, LocalDateTime.now()));

        int current = item.getQuantity();
        if (current < quantity) {
            return false;
        }

        item.setQuantity(current - quantity);
        item.setUpdatedAt(LocalDateTime.now());
        inventoryRepository.save(item);
        return true;
    }

    @Transactional
    public void release(String productId, int quantity) {
        InventoryItem item = inventoryRepository.findById(productId)
                .orElse(new InventoryItem(productId, 0, LocalDateTime.now()));

        item.setQuantity(item.getQuantity() + quantity);
        item.setUpdatedAt(LocalDateTime.now());
        inventoryRepository.save(item);
    }

    @Transactional
    public InventoryItem upsert(String productId, int quantity) {
        InventoryItem item = inventoryRepository.findById(productId)
                .orElse(new InventoryItem(productId, 0, LocalDateTime.now()));
        item.setQuantity(quantity);
        item.setUpdatedAt(LocalDateTime.now());
        return inventoryRepository.save(item);
    }

    @Transactional(readOnly = true)
    public Map<String, Integer> currentStock() {
        Map<String, Integer> result = new LinkedHashMap<>();
        List<InventoryItem> items = inventoryRepository.findAllByOrderByProductIdAsc();
        for (InventoryItem item : items) {
            result.put(item.getProductId(), item.getQuantity());
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<InventoryItem> currentStockItems() {
        return inventoryRepository.findAllByOrderByProductIdAsc();
    }

    private void initializeIfMissing(String productId, int quantity) {
        if (inventoryRepository.existsById(productId)) {
            return;
        }
        inventoryRepository.save(new InventoryItem(productId, quantity, LocalDateTime.now()));
    }
}
