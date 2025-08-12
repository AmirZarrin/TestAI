package com.ai.testai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_sequence")
    @SequenceGenerator(name = "person_sequence", sequenceName = "person_sequence", allocationSize = 1)
    private Long id;
    
    private String firstName;
    private String lastName;
    private String nationalCode;
    private String phoneNumber;
    private String email;
    private LocalDate birthDate;
    
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Credit> credits = new ArrayList<>();
    
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupportTicket> supportTickets = new ArrayList<>();
    
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoyaltyReward> loyaltyRewards = new ArrayList<>();
    
    // Helper methods for bidirectional relationships
    public void addCredit(Credit credit) {
        credits.add(credit);
        credit.setPerson(this);
    }
    
    public void addSupportTicket(SupportTicket supportTicket) {
        supportTickets.add(supportTicket);
        supportTicket.setPerson(this);
    }
    
    public void addLoyaltyReward(LoyaltyReward loyaltyReward) {
        loyaltyRewards.add(loyaltyReward);
        loyaltyReward.setPerson(this);
    }
}
