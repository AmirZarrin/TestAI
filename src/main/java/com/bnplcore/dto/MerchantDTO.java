package com.bnplcore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MerchantDTO {
    private Long id;
    
    @NotBlank(message = "Business name is required")
    private String businessName;
    
    @NotBlank(message = "Tax ID is required")
    private String taxId;
    
    @NotBlank(message = "Registration number is required")
    private String registrationNumber;
    
    @NotBlank(message = "Contact person name is required")
    private String contactPersonName;
    
    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid email format")
    private String contactEmail;
    
    @NotBlank(message = "Contact phone is required")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,}$", message = "Invalid phone number format")
    private String contactPhone;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String status;
    private LocalDateTime registrationDate;
    private LocalDateTime lastUpdated;
    private String businessType;
    private String website;
    private String description;
    
    // Additional fields
    private Double commissionRate;
    private String paymentTerms;
    private String bankAccountDetails;
    private String taxRegistrationNumber;
    private String legalEntityName;
    private String businessRegistrationDocument;
    private String taxClearanceCertificate;
    private String bankAccountVerificationDocument;
}
