package com.example.paymentsaga;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public boolean pay(String orderId, Integer amount) {
        // 데모용: 50000 초과면 실패
        return amount != null && amount <= 50000;
    }
}