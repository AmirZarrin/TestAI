package com.ai.testai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "installment_payments")
public class InstallmentPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "installment_payment_sequence")
    @SequenceGenerator(name = "installment_payment_sequence", sequenceName = "installment_payment_sequence", allocationSize = 1)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "installment_plan_id", nullable = false)
    private InstallmentPlan installmentPlan;
    
    private Integer installmentNumber;
    private BigDecimal amount;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private String status;
    private String paymentReference;
    
    @OneToOne(mappedBy = "installmentPayment", cascade = CascadeType.ALL)
    private Transaction transaction;
}
