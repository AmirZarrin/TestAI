package com.bnplcore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ticket_comments")
public class TicketComment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_comment_sequence")
    @SequenceGenerator(name = "ticket_comment_sequence", sequenceName = "ticket_comment_sequence", allocationSize = 1)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private SupportTicket ticket;
    
    private String content;
    private String authorId;
    private String authorType; // CUSTOMER, SUPPORT_AGENT, SYSTEM
    private Boolean isInternal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
