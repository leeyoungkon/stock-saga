package com.example.stocksaga;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SagaEventLogRepository extends JpaRepository<SagaEventLog, Long> {

    List<SagaEventLog> findTop200ByOrderByCreatedAtDesc();
}
