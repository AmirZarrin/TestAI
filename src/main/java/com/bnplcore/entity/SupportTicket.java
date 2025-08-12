package com.bnplcore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "support_tickets")
public class SupportTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "support_ticket_sequence")
    @SequenceGenerator(name = "support_ticket_sequence", sequenceName = "support_ticket_sequence", allocationSize = 1)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
    
    private String title;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String priority;
    private String assignedTo;
    
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketComment> comments = new ArrayList<>();
    
    // Helper method for bidirectional relationship
    public void addComment(TicketComment comment) {
        comments.add(comment);
        comment.setTicket(this);
    }
}
