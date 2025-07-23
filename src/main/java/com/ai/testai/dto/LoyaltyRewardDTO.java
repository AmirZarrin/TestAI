package com.ai.testai.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class LoyaltyRewardDTO {
    private Long rewardId;
    private Integer points;
    private LocalDate expiryDate;
    private String status;
    private String rewardType;
}
