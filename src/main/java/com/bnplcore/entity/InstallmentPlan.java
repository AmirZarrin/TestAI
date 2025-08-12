package com.bnplcore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "installment_plans")
public class InstallmentPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "installment_plan_sequence")
    @SequenceGenerator(name = "installment_plan_sequence", sequenceName = "installment_plan_sequence", allocationSize = 1)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;
    
    @ManyToOne
    @JoinColumn(name = "credit_id", nullable = false)
    private Credit credit;
    
    private Integer installmentCount;
    private BigDecimal totalAmount;
    private BigDecimal monthlyPayment;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    
    @OneToMany(mappedBy = "installmentPlan", cascade = CascadeType.ALL)
    private List<InstallmentPayment> payments;
    
    @OneToMany(mappedBy = "installmentPlan", cascade = CascadeType.ALL)
    private List<Penalty> penalties;
}
