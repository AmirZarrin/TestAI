package com.ai.testai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TicketCommentDTO {
    private Long id;
    
    @NotNull(message = "Ticket ID is required")
    private Long ticketId;
    
    @NotNull(message = "Author ID is required")
    private Long authorId;
    
    @NotBlank(message = "Author type is required")
    private String authorType; // CUSTOMER, SUPPORT_AGENT, SYSTEM
    
    @NotBlank(message = "Author name is required")
    private String authorName;
    
    @NotBlank(message = "Author email is required")
    private String authorEmail;
    
    @NotBlank(message = "Comment cannot be empty")
    @Size(max = 5000, message = "Comment must be less than 5000 characters")
    private String comment;
    
    private String contentType; // TEXT, HTML, MARKDOWN
    private List<AttachmentDTO> attachments;
    private String internalNote; // Only visible to support agents
    private boolean isInternal; // Whether the comment is internal only
    private String status; // VISIBLE, HIDDEN, DELETED
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String metadata; // JSON string for additional data
    
    // For threaded comments
    private Long parentCommentId;
    private List<TicketCommentDTO> replies;
}
