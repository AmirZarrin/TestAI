package com.bnplcore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class TransactionDTO {
    private Long id;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @NotNull(message = "Type is required")
    private String type;  // e.g., PAYMENT, REFUND, CHARGEBACK
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime transactionDate;
    
    @Size(max = 50, message = "Status cannot exceed 50 characters")
    private String status;  // e.g., PENDING, COMPLETED, FAILED, REFUNDED
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    private Long merchantId;
    private String merchantName;
    private String customerName;
    private String description;
    private String referenceId;
    private String paymentMethod;
    private String paymentGateway;
    private String currency;
    private BigDecimal feeAmount;
    private BigDecimal netAmount;
    private String errorMessage;
    private Map<String, Object> metadata;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // For refunds
    private Long originalTransactionId;
    private String refundReason;
    
    // For chargebacks
    private Boolean isChargeback;
    private LocalDateTime chargebackDate;
    private String chargebackReason;
    
    // For settlements
    private Long settlementId;
    private LocalDateTime settlementDate;
    
    // For installments
    private Long installmentPlanId;
    private Integer installmentNumber;
    private Integer totalInstallments;
}
