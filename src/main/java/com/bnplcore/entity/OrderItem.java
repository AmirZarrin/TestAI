package com.bnplcore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_sequence")
    @SequenceGenerator(name = "order_item_sequence", sequenceName = "order_item_sequence", allocationSize = 1)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "credit_id", nullable = false)
    private Credit credit;
    
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    
    @OneToOne(mappedBy = "orderItem", cascade = CascadeType.ALL)
    private InstallmentPlan installmentPlan;
}
