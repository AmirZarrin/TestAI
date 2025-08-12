package com.bnplcore.dto;

import lombok.Data;

@Data
public class CreditInfoDTO {
    private Long customerId;
    private Integer creditScore;
    private Double creditLimit;
    private Double availableCredit;
}
