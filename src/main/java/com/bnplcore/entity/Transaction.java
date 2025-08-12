package com.bnplcore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_sequence")
    @SequenceGenerator(name = "transaction_sequence", sequenceName = "transaction_sequence", allocationSize = 1)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "credit_id", nullable = false)
    private Credit credit;
    
    @OneToOne
    @JoinColumn(name = "installment_payment_id")
    private InstallmentPayment installmentPayment;
    
    private String transactionType;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String status;
    private String referenceNumber;
    private String description;
}
