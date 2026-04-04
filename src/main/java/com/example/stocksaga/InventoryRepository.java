package com.example.stocksaga;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<InventoryItem, String> {

    List<InventoryItem> findAllByOrderByProductIdAsc();
}
