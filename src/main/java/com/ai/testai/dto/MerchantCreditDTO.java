package com.ai.testai.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MerchantCreditDTO {
    private Long id;
    private Long merchantId;
    private String merchantName;
    private String creditType;  // e.g., "LINE_OF_CREDIT", "OVERDRAFT", "TERM_LOAN"
    private BigDecimal creditLimit;
    private BigDecimal availableCredit;
    private BigDecimal usedCredit;
    private BigDecimal interestRate;
    private String interestType; // e.g., "FIXED", "VARIABLE"
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;      // e.g., "ACTIVE", "SUSPENDED", "CLOSED"
    private String currency;
    private Integer termMonths;
    private BigDecimal minMonthlyPayment;
    private BigDecimal totalRepaid;
    private BigDecimal outstandingBalance;
    private LocalDate nextPaymentDueDate;
    private Integer daysPastDue;
    private String collateralType;
    private BigDecimal collateralValue;
    private String paymentFrequency; // e.g., "MONTHLY", "QUARTERLY"
    private String creditScore;
    private String riskRating;
    private LocalDateTime lastPaymentDate;
    private BigDecimal lastPaymentAmount;
    private String termsAndConditions;
    private String notes;
    private String approvalReference;
    private String approvedBy;
    private LocalDateTime approvalDate;
    private String reviewFrequency;
    private LocalDate nextReviewDate;
    private String creditOfficer;
    private String productCode;
    private String accountNumber;
    private String branchCode;
    private String creditPurpose;
    private Boolean isSecured;
    private String securityDetails;
    private String repaymentMethod;
    private String earlyRepaymentTerms;
    private String latePaymentPenalties;
    private String feeStructure;
    private String insuranceDetails;
    private String documentationStatus;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
