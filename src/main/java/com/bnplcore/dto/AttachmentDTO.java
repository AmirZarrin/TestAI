package com.bnplcore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttachmentDTO {
    private Long id;
    
    @NotBlank(message = "File name is required")
    @Size(max = 255, message = "File name must be less than 255 characters")
    private String fileName;
    
    @NotBlank(message = "File type is required")
    private String fileType;
    
    @NotNull(message = "File size is required")
    private Long fileSize;
    
    @NotBlank(message = "File URL or path is required")
    private String fileUrl;
    
    private String description;
    private String uploaderId;
    private String uploaderName;
    private String uploaderType; // CUSTOMER, SUPPORT_AGENT, SYSTEM
    private String storageType; // LOCAL, S3, GOOGLE_DRIVE, etc.
    private String checksum;
    private String mimeType;
    private String status; // UPLOADED, PROCESSING, READY, ERROR
    private String errorMessage;
    private Boolean isPublic;
    private String accessLevel; // PUBLIC, PRIVATE, RESTRICTED
    private String metadata; // JSON string for additional data
    private LocalDateTime uploadedAt;
    private LocalDateTime expiresAt;
    private String dimensions; // For images/videos: "width x height"
    private Integer duration; // For audio/video: duration in seconds
    private String thumbnailUrl; // URL to a thumbnail for images/videos
}
