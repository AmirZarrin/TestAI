package com.ai.testai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "loyalty_rewards")
public class LoyaltyReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
    
    private String rewardType;
    private Integer points;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String status;
    private String description;
}
