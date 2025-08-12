package com.bnplcore.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MerchantProductDTO {
    private Long id;
    
    @NotBlank(message = "Product name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;
    
    @NotBlank(message = "Currency is required")
    private String currency;
    
    private String category;
    private String sku;
    private String barcode;
    
    @Positive(message = "Stock quantity must be positive")
    private Integer stockQuantity;
    
    private Boolean isActive;
    private String imageUrl;
    private String specifications; // JSON string of product specifications
    private String[] tags;
    private String[] imageUrls;
    private String brand;
    private String model;
    private String manufacturer;
    private String weight;
    private String dimensions;
    private String color;
    private String material;
    private String warrantyPeriod;
    private String returnPolicy;
}
