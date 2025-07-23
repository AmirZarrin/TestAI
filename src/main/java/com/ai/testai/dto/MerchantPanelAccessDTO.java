package com.ai.testai.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MerchantPanelAccessDTO {
    private Long id;
    
    @NotNull(message = "Merchant ID is required")
    private Long merchantId;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String role;  // e.g., "ADMIN", "MANAGER", "STAFF"
    private List<String> permissions;
    private Boolean isActive;
    private LocalDateTime lastLogin;
    private Integer failedLoginAttempts;
    private Boolean isLocked;
    private LocalDateTime passwordExpiryDate;
    private Boolean forcePasswordChange;
    private String department;
    private String position;
    private String timezone;
    private String language;
    private String status;  // e.g., "ACTIVE", "PENDING_ACTIVATION", "SUSPENDED"
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private String notes;
    private String profilePictureUrl;
    private String signature;
    private Boolean twoFactorEnabled;
    private String twoFactorMethod;  // e.g., "SMS", "EMAIL", "AUTHENTICATOR_APP"
    private String phoneNumberVerified;
    private String emailVerified;
    private LocalDateTime lastPasswordChangedAt;
    private String lastIpAddress;
    private String lastUserAgent;
    private String preferredContactMethod;
    private String securityQuestion1;
    private String securityAnswer1;
    private String securityQuestion2;
    private String securityAnswer2;
    private String customField1;
    private String customField2;
    private String customField3;
}
