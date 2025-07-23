package com.ai.testai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FaqDTO {
    private Long id;
    
    @NotBlank(message = "Question is required")
    @Size(max = 500, message = "Question must be less than 500 characters")
    private String question;
    
    @NotBlank(message = "Answer is required")
    @Size(max = 10000, message = "Answer must be less than 10000 characters")
    private String answer;
    
    @NotBlank(message = "Category is required")
    private String category;
    
    private String subcategory;
    private List<String> tags;
    private Integer viewCount;
    private Integer helpfulCount;
    private Integer notHelpfulCount;
    private String status; // DRAFT, PUBLISHED, ARCHIVED
    private String authorId;
    private String authorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String relatedFaqs; // Comma-separated list of related FAQ IDs
    private String metadata; // JSON string for additional data
    private String language; // e.g., en, fa, etc.
    private Integer sortOrder;
    private String seoKeywords;
    private String seoDescription;
    private String lastReviewedBy;
    private LocalDateTime lastReviewedAt;
}
