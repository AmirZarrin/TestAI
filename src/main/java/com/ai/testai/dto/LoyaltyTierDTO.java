package com.ai.testai.dto;

import lombok.Data;

@Data
public class LoyaltyTierDTO {
    private Long tierId;
    private String name;
    private String description;
    private Integer minPoints;
    private Double discountRate;
    private String[] benefits;
}
