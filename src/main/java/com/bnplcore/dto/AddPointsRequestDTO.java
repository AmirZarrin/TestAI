package com.bnplcore.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddPointsRequestDTO {
    @NotNull(message = "Points amount is required")
    @Positive(message = "Points must be a positive number")
    private Integer points;
    
    private String reason;
    private String referenceId;
}
