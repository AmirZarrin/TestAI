package com.bnplcore.controller;

import com.bnplcore.dto.AttachmentDTO;
import com.bnplcore.service.SupportService;
import com.bnplcore.dto.FaqDTO;
import com.bnplcore.dto.SupportTicketDTO;
import com.bnplcore.dto.TicketCommentDTO;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/support")
public class SupportController {
    
    private final SupportService supportService;
    private final ModelMapper modelMapper;
    
    @Autowired
    public SupportController(SupportService supportService, ModelMapper modelMapper) {
        this.supportService = supportService;
        this.modelMapper = modelMapper;
    }
    
    @PostMapping("/tickets")
    public ResponseEntity<SupportTicketDTO> createTicket(
            @Valid @RequestBody SupportTicketDTO ticketDTO) {
        SupportTicketDTO createdTicket = supportService.createTicket(ticketDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }
    
    @GetMapping("/tickets/{id}")
    public ResponseEntity<SupportTicketDTO> getTicket(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean includeComments,
            @RequestParam(required = false, defaultValue = "false") boolean includeAttachments) {
        SupportTicketDTO ticket = supportService.getTicket(id, includeComments, includeAttachments);
        return ResponseEntity.ok(ticket);
    }
    
    @GetMapping("/customers/{customerId}/tickets")
    public ResponseEntity<Page<SupportTicketDTO>> getCustomerTickets(
            @PathVariable Long customerId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            @RequestParam(required = false, defaultValue = "false") boolean includeClosed,
            Pageable pageable) {
        
        Page<SupportTicketDTO> tickets = supportService.getCustomerTickets(
            customerId, status, priority, category, fromDate, toDate, includeClosed, pageable);
        return ResponseEntity.ok(tickets);
    }
    
    @PutMapping("/tickets/{id}")
    public ResponseEntity<SupportTicketDTO> updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody SupportTicketDTO updateDTO) {
        SupportTicketDTO updatedTicket = supportService.updateTicket(id, updateDTO);
        return ResponseEntity.ok(updatedTicket);
    }
    
    @PatchMapping("/tickets/{id}")
    public ResponseEntity<SupportTicketDTO> partiallyUpdateTicket(
            @PathVariable Long id,
            @RequestBody SupportTicketDTO updateDTO) {
        SupportTicketDTO updatedTicket = supportService.partiallyUpdateTicket(id, updateDTO);
        return ResponseEntity.ok(updatedTicket);
    }
    
    @PostMapping("/tickets/{id}/comments")
    public ResponseEntity<TicketCommentDTO> addComment(
            @PathVariable Long id,
            @Valid @RequestBody TicketCommentDTO commentDTO) {
        commentDTO.setTicketId(id);
        TicketCommentDTO createdComment = supportService.addComment(commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }
    
    @PostMapping(value = "/tickets/{id}/attachments", consumes = "multipart/form-data")
    public ResponseEntity<AttachmentDTO> addAttachment(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String description) {
        
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }
        
        try {
            // Create attachment DTO with file details
            AttachmentDTO attachmentDTO = new AttachmentDTO();
            attachmentDTO.setFileName(file.getOriginalFilename());
            attachmentDTO.setFileType(file.getContentType());
            attachmentDTO.setFileSize(file.getSize());
            attachmentDTO.setDescription(description);
            attachmentDTO.setUploadedAt(LocalDateTime.now());
            
            // In a real implementation, you would save the file to storage
            // and set the URL. For example:
            // String fileUrl = fileStorageService.storeFile(file);
            // attachmentDTO.setFileUrl(fileUrl);
            
            // For now, we'll just return the DTO with basic file info
            return ResponseEntity.status(HttpStatus.CREATED).body(attachmentDTO);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to process file upload: " + e.getMessage(), e);
        }
    }
    
    @GetMapping("/tickets/{id}/comments")
    public ResponseEntity<Page<TicketCommentDTO>> getTicketComments(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean includeInternal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            Pageable pageable) {
        
        Page<TicketCommentDTO> comments = supportService.getTicketComments(
            id, includeInternal, fromDate, toDate, pageable);
        return ResponseEntity.ok(comments);
    }
    
    @GetMapping("/tickets/{ticketId}/comments/{commentId}")
    public ResponseEntity<TicketCommentDTO> getTicketComment(
            @PathVariable Long ticketId,
            @PathVariable Long commentId) {
        TicketCommentDTO comment = supportService.getTicketComment(ticketId, commentId);
        return ResponseEntity.ok(comment);
    }
    
    @PutMapping("/tickets/{id}/status")
    public ResponseEntity<SupportTicketDTO> updateTicketStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String resolutionSummary) {
        SupportTicketDTO updatedTicket = supportService.updateTicketStatus(id, status, resolutionSummary);
        return ResponseEntity.ok(updatedTicket);
    }
    
    @PutMapping("/tickets/{id}/assign")
    public ResponseEntity<SupportTicketDTO> assignTicket(
            @PathVariable Long id,
            @RequestParam String assigneeId) {
        SupportTicketDTO updatedTicket = supportService.assignTicket(id, assigneeId);
        return ResponseEntity.ok(updatedTicket);
    }
    
    @PutMapping("/tickets/{id}/priority")
    public ResponseEntity<SupportTicketDTO> updateTicketPriority(
            @PathVariable Long id,
            @RequestParam String priority) {
        SupportTicketDTO updatedTicket = supportService.updateTicketPriority(id, priority);
        return ResponseEntity.ok(updatedTicket);
    }
    
    @GetMapping("/faqs")
    public ResponseEntity<Page<FaqDTO>> getFrequentlyAskedQuestions(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "false") boolean includeUnpublished,
            Pageable pageable) {
        
        Page<FaqDTO> faqs = supportService.getFrequentlyAskedQuestions(
            category, query, includeUnpublished, pageable);
        return ResponseEntity.ok(faqs);
    }
    
    @GetMapping("/faqs/categories")
    public ResponseEntity<List<String>> getFaqCategories() {
        List<String> categories = supportService.getFaqCategories();
        return ResponseEntity.ok(categories);
    }
    
    @PostMapping("/faqs/{id}/feedback")
    public ResponseEntity<Void> submitFaqFeedback(
            @PathVariable Long id,
            @RequestParam boolean wasHelpful) {
        supportService.recordFaqFeedback(id, wasHelpful);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/tickets/search")
    public ResponseEntity<Page<SupportTicketDTO>> searchTickets(
            @RequestParam String query,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            Pageable pageable) {
        
        Page<SupportTicketDTO> results = supportService.searchTickets(
            query, status, priority, category, fromDate, toDate, pageable);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getSupportDashboardStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        Map<String, Object> stats = supportService.getDashboardStats(
            startDate != null ? startDate.atStartOfDay() : null,
            endDate != null ? endDate.plusDays(1).atStartOfDay() : null);
        return ResponseEntity.ok(stats);
    }
}
