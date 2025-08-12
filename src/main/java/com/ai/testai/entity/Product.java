package com.ai.testai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
    @SequenceGenerator(name = "product_sequence", sequenceName = "product_sequence", allocationSize = 1)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;
    
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String category;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
}
