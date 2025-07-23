package com.ai.testai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SupportTicketDTO {
    private Long id;
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be less than 200 characters")
    private String title;
    
    @NotBlank(message = "Description is required")
    @Size(max = 5000, message = "Description must be less than 5000 characters")
    private String description;
    
    private String status; // OPEN, IN_PROGRESS, WAITING_CUSTOMER, RESOLVED, CLOSED
    private String priority; // LOW, MEDIUM, HIGH, URGENT
    private String category; // PAYMENT, ACCOUNT, TECHNICAL, GENERAL, etc.
    private String assignedTo;
    private String customerName;
    private String customerEmail;
    private String ticketNumber;
    private LocalDateTime dueDate;
    private LocalDateTime resolvedAt;
    private String resolutionSummary;
    private List<String> tags;
    private List<AttachmentDTO> attachments;
    private List<TicketCommentDTO> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private String source; // WEB, MOBILE_APP, EMAIL, PHONE
    private String satisfactionRating;
    private String customFields; // JSON string for additional fields
}
