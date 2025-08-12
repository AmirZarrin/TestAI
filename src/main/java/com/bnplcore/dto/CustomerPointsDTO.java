package com.bnplcore.dto;

import lombok.Data;

@Data
public class CustomerPointsDTO {
    private Long customerId;
    private int totalPoints;
    private int availablePoints;
    private int pendingPoints;
    private String tier;
    
    // Additional fields for points summary
    private int pointsExpiringSoon;
    private String nextTier;
    private int pointsToNextTier;
}
