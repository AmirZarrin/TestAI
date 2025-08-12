package com.bnplcore.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CustomerDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String nationalCode;
    private String phoneNumber;
    private String email;
    private LocalDate birthDate;
}
