package com.bnplcore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "merchant_panel_access")
public class MerchantPanelAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "merchant_panel_access_id_seq")
    @SequenceGenerator(name = "merchant_panel_access_id_seq", sequenceName = "merchant_panel_access_id_seq", allocationSize = 1)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;
    
    private String username;
    private String passwordHash;
    private String email;
    private String role;
    private LocalDateTime lastLogin;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
