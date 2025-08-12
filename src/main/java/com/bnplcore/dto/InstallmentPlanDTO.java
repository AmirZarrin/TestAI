package com.bnplcore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InstallmentPlanDTO {
    private Long id;
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    @NotNull(message = "Order ID is required")
    private Long orderId;
    
    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total amount must be greater than 0")
    private BigDecimal totalAmount;
    
    @NotNull(message = "Monthly payment is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly payment must be greater than 0")
    private BigDecimal monthlyPayment;
    
    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.0", message = "Interest rate cannot be negative")
    private BigDecimal interestRate;
    
    @NotNull(message = "Term is required")
    private Integer termInMonths;
    
    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;
    
    @Size(max = 50, message = "Status cannot exceed 50 characters")
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // Additional fields for display purposes
    private BigDecimal paidAmount;
    private BigDecimal remainingAmount;
    private Integer paymentsMade;
    private Integer paymentsRemaining;
}
