package com.bnplcore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "penalties")
public class Penalty {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "penalty_sequence")
    @SequenceGenerator(name = "penalty_sequence", sequenceName = "penalty_sequence", allocationSize = 1)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "installment_plan_id", nullable = false)
    private InstallmentPlan installmentPlan;
    
    private BigDecimal amount;
    private String reason;
    private LocalDate penaltyDate;
    private String status;
    private LocalDate paymentDueDate;
    private LocalDate paymentDate;
}
