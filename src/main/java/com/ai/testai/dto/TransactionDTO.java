package com.ai.testai.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private Long transactionId;
    private BigDecimal amount;
    private String type;
    private LocalDateTime date;
    private String status;
}
