package com.ai.testai.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddPointsResponseDTO {
    private Long customerId;
    private int pointsAdded;
    private String transactionId;
    private int newBalance;
    private LocalDateTime transactionDate;
    private String status;
    private String message;
}
