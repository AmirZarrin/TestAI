package com.bnplcore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RedeemRewardRequestDTO {
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    private String redemptionCode;
}
