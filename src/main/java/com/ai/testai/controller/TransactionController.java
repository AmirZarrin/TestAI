package com.ai.testai.controller;

import com.ai.testai.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> createTransaction(
            @RequestBody Map<String, Object> transactionRequest) {
        return ResponseEntity.ok(transactionService.createTransaction(transactionRequest));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransaction(id));
    }
    
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Map<String, Object>>> getCustomerTransactions(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(transactionService.getCustomerTransactions(customerId));
    }
    
    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<List<Map<String, Object>>> getMerchantTransactions(
            @PathVariable Long merchantId) {
        return ResponseEntity.ok(transactionService.getMerchantTransactions(merchantId));
    }
    
    @PostMapping("/{id}/refund")
    public ResponseEntity<Map<String, Object>> processRefund(
            @PathVariable Long id,
            @RequestBody Map<String, Object> refundRequest) {
        return ResponseEntity.ok(transactionService.processRefund(id, refundRequest));
    }
    
    @PostMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateTransactionStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusRequest) {
        return ResponseEntity.ok(transactionService.updateTransactionStatus(id, statusRequest.get("status")));
    }
}
