package com.bnplcore.service;

import com.bnplcore.controller.TransactionController;
import com.bnplcore.dto.TransactionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TransactionService {
    // In-memory storage for demo purposes
    private final ConcurrentHashMap<Long, TransactionDTO> transactions = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1000);
    
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        // Set ID and timestamps
        transactionDTO.setId(idGenerator.incrementAndGet());
        transactionDTO.setTransactionDate(LocalDateTime.now());
        transactionDTO.setCreatedAt(LocalDateTime.now());
        transactionDTO.setUpdatedAt(LocalDateTime.now());
        
        // Set default status if not provided
        if (transactionDTO.getStatus() == null) {
            transactionDTO.setStatus("PENDING");
        }
        
        // Save to in-memory storage
        transactions.put(transactionDTO.getId(), transactionDTO);
        
        return transactionDTO;
    }
    
    public TransactionDTO getTransaction(Long id) {
        TransactionDTO transaction = transactions.get(id);
        if (transaction == null) {
            throw new RuntimeException("Transaction not found with id: " + id);
        }
        return transaction;
    }
    
    public Page<TransactionDTO> getAllTransactions(
            String type, 
            String status, 
            LocalDateTime startDate, 
            LocalDateTime endDate,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            Pageable pageable) {
        
        List<TransactionDTO> filtered = new ArrayList<>(transactions.values());
        
        // Apply filters
        filtered = filtered.stream()
            .filter(t -> type == null || type.equals(t.getType()))
            .filter(t -> status == null || status.equals(t.getStatus()))
            .filter(t -> startDate == null || !t.getTransactionDate().isBefore(startDate))
            .filter(t -> endDate == null || t.getTransactionDate().isBefore(endDate))
            .filter(t -> minAmount == null || t.getAmount().compareTo(minAmount) >= 0)
            .filter(t -> maxAmount == null || t.getAmount().compareTo(maxAmount) <= 0)
            .toList();
        
        // Apply pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filtered.size());
        
        if (start > filtered.size()) {
            return new PageImpl<>(List.of(), pageable, filtered.size());
        }
        
        return new PageImpl<>(
            filtered.subList(start, end), 
            pageable, 
            filtered.size()
        );
    }
    
    public Page<TransactionDTO> getCustomerTransactions(
            Long customerId,
            String type,
            String status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {
        
        List<TransactionDTO> filtered = new ArrayList<>(transactions.values());
        
        // Apply filters
        filtered = filtered.stream()
            .filter(t -> customerId.equals(t.getCustomerId()))
            .filter(t -> type == null || type.equals(t.getType()))
            .filter(t -> status == null || status.equals(t.getStatus()))
            .filter(t -> startDate == null || !t.getTransactionDate().isBefore(startDate))
            .filter(t -> endDate == null || t.getTransactionDate().isBefore(endDate))
            .toList();
        
        // Apply pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filtered.size());
        
        if (start > filtered.size()) {
            return new PageImpl<>(List.of(), pageable, filtered.size());
        }
        
        return new PageImpl<>(
            filtered.subList(start, end), 
            pageable, 
            filtered.size()
        );
    }
    
    public Page<TransactionDTO> getMerchantTransactions(
            Long merchantId,
            String type,
            String status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {
        
        List<TransactionDTO> filtered = new ArrayList<>(transactions.values());
        
        // Apply filters
        filtered = filtered.stream()
            .filter(t -> merchantId.equals(t.getMerchantId()))
            .filter(t -> type == null || type.equals(t.getType()))
            .filter(t -> status == null || status.equals(t.getStatus()))
            .filter(t -> startDate == null || !t.getTransactionDate().isBefore(startDate))
            .filter(t -> endDate == null || t.getTransactionDate().isBefore(endDate))
            .toList();
        
        // Apply pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filtered.size());
        
        if (start > filtered.size()) {
            return new PageImpl<>(List.of(), pageable, filtered.size());
        }
        
        return new PageImpl<>(
            filtered.subList(start, end), 
            pageable, 
            filtered.size()
        );
    }
    
    public TransactionDTO processRefund(
            Long id, 
            TransactionController.RefundRequestDTO refundRequest) {
        
        // Get the original transaction
        TransactionDTO original = getTransaction(id);
        
        // Create refund transaction
        TransactionDTO refund = new TransactionDTO();
        refund.setId(idGenerator.incrementAndGet());
        refund.setType("REFUND");
        refund.setStatus("COMPLETED");
        //refund.setAmount(refundRequest.getAmount());
        refund.setCustomerId(original.getCustomerId());
        refund.setMerchantId(original.getMerchantId());
        refund.setOriginalTransactionId(original.getId());
        //refund.setRefundReason(refundRequest.getReason());
        refund.setTransactionDate(LocalDateTime.now());
        refund.setCreatedAt(LocalDateTime.now());
        refund.setUpdatedAt(LocalDateTime.now());
        
        // Save the refund
        transactions.put(refund.getId(), refund);
        
        return refund;
    }
    
    public TransactionDTO updateTransactionStatus(
            Long id, 
            String status,
            String notes) {
        
        TransactionDTO transaction = getTransaction(id);
        transaction.setStatus(status);
        transaction.setUpdatedAt(LocalDateTime.now());
        
        // Update in storage
        transactions.put(id, transaction);
        
        return transaction;
    }
}
