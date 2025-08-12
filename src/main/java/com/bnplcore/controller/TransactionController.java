package com.bnplcore.controller;

import com.bnplcore.dto.TransactionDTO;
import com.bnplcore.service.TransactionService;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(
            @Valid @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO createdTransaction = transactionService.createTransaction(transactionDTO);
        return ResponseEntity.ok(createdTransaction);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable Long id) {
        TransactionDTO transaction = transactionService.getTransaction(id);
        return ResponseEntity.ok(transaction);
    }
    
    @GetMapping
    public ResponseEntity<Page<TransactionDTO>> getAllTransactions(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            Pageable pageable) {
        
        Page<TransactionDTO> transactions = transactionService.getAllTransactions(
            type, status, startDate, endDate, minAmount, maxAmount, pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<TransactionDTO>> getCustomerTransactions(
            @PathVariable Long customerId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        
        Page<TransactionDTO> transactions = transactionService.getCustomerTransactions(
            customerId, type, status, startDate, endDate, pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<Page<TransactionDTO>> getMerchantTransactions(
            @PathVariable Long merchantId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        
        Page<TransactionDTO> transactions = transactionService.getMerchantTransactions(
            merchantId, type, status, startDate, endDate, pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @PostMapping("/{id}/refund")
    public ResponseEntity<TransactionDTO> processRefund(
            @PathVariable Long id,
            @RequestBody RefundRequestDTO refundRequest) {
        
        TransactionDTO refundTransaction = transactionService.processRefund(id, refundRequest);
        return ResponseEntity.ok(refundTransaction);
    }
    
    @PostMapping("/{id}/status")
    public ResponseEntity<TransactionDTO> updateTransactionStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest statusUpdate) {
        
        TransactionDTO updatedTransaction = transactionService.updateTransactionStatus(
            id, statusUpdate.getStatus(), statusUpdate.getNotes());
        return ResponseEntity.ok(updatedTransaction);
    }
    
    // DTOs for request bodies
    @Data
    public static class RefundRequestDTO {
        @NotNull(message = "Refund amount is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
        private BigDecimal amount;
        
        private String reason;
        private String referenceId;
    }
    
    @Data
    public static class StatusUpdateRequest {
        @NotBlank(message = "Status is required")
        private String status;
        
        private String notes;
    }
}
