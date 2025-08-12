package com.bnplcore.entity;

import java.util.List;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "credits")
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credit_sequence")
    @SequenceGenerator(name = "credit_sequence", sequenceName = "credit_sequence", allocationSize = 1)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
    
    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;
    
    private BigDecimal creditScore;
    private BigDecimal creditLimit;
    private BigDecimal usedCredit;
    private BigDecimal availableCredit;
    
    @OneToMany(mappedBy = "credit", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
    
    @OneToMany(mappedBy = "credit", cascade = CascadeType.ALL)
    private List<InstallmentPlan> installmentPlans;
    
    @OneToMany(mappedBy = "credit", cascade = CascadeType.ALL)
    private List<Transaction> transactions;
}
