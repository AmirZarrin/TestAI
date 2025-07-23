package com.ai.testai.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IssueRewardRequestDTO {
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    @NotNull(message = "Points are required")
    private Integer points;
    
    private String rewardType;
    private String description;
}
