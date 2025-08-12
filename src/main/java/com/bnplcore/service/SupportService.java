package com.bnplcore.service;

import com.bnplcore.dto.FaqDTO;
import com.bnplcore.dto.SupportTicketDTO;
import com.bnplcore.dto.TicketCommentDTO;
import com.bnplcore.exception.ResourceNotFoundException;
import com.bnplcore.repository.SupportTicketRepository;
import com.bnplcore.repository.TicketCommentRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class SupportService {

    private final SupportTicketRepository supportTicketRepository;
    private final TicketCommentRepository ticketCommentRepository;
    private final ModelMapper modelMapper;

    // In-memory storage for demonstration purposes
    private final Map<Long, SupportTicketDTO> ticketCache = new ConcurrentHashMap<>();
    private final Map<Long, TicketCommentDTO> commentCache = new ConcurrentHashMap<>();
    private final Map<Long, FaqDTO> faqCache = new ConcurrentHashMap<>();
    private final AtomicLong ticketIdGenerator = new AtomicLong(1000);
    private final AtomicLong commentIdGenerator = new AtomicLong(10000);
    private final AtomicLong faqIdGenerator = new AtomicLong(100);

    @Autowired
    public SupportService(
            SupportTicketRepository supportTicketRepository,
            TicketCommentRepository ticketCommentRepository,
            ModelMapper modelMapper) {
        this.supportTicketRepository = supportTicketRepository;
        this.ticketCommentRepository = ticketCommentRepository;
        this.modelMapper = modelMapper;

        // Initialize with sample data for demonstration
        initializeSampleData();
    }

    public SupportTicketDTO createTicket(SupportTicketDTO ticketDTO) {
        // Generate a ticket number (e.g., TKT-2023-0001)
        String ticketNumber = String.format("TKT-%s-%04d",
                LocalDate.now().getYear(),
                ticketIdGenerator.incrementAndGet());

        // Set default values if not provided
        if (ticketDTO.getStatus() == null) {
            ticketDTO.setStatus("OPEN");
        }
        if (ticketDTO.getPriority() == null) {
            ticketDTO.setPriority("MEDIUM");
        }

        // Set timestamps
        LocalDateTime now = LocalDateTime.now();
        ticketDTO.setCreatedAt(now);
        ticketDTO.setUpdatedAt(now);
        ticketDTO.setTicketNumber(ticketNumber);

        // In a real application, we would save to the database
        // SupportTicket ticket = modelMapper.map(ticketDTO, SupportTicket.class);
        // ticket = supportTicketRepository.save(ticket);

        // For demo, store in memory cache
        if (ticketDTO.getId() == null) {
            ticketDTO.setId(ticketIdGenerator.incrementAndGet());
        }
        ticketCache.put(ticketDTO.getId(), ticketDTO);

        return ticketDTO;
    }

    public SupportTicketDTO getTicket(Long ticketId, boolean includeComments, boolean includeAttachments) {
        // In a real application, we would fetch from the database
        // SupportTicket ticket = supportTicketRepository.findById(ticketId)
        //     .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));
        // SupportTicketDTO dto = modelMapper.map(ticket, SupportTicketDTO.class);

        // For demo, get from cache or throw if not found
        SupportTicketDTO ticket = ticketCache.get(ticketId);
        if (ticket == null) {
            throw new ResourceNotFoundException("Ticket not found with id: " + ticketId);
        }

        // Load related data if requested
        if (includeComments) {
            List<TicketCommentDTO> comments = getTicketComments(
                    ticketId, false, null, null, Pageable.unpaged()).getContent();
            ticket.setComments(comments);
        }

        // In a real app, we would also load attachments here if includeAttachments is true

        return ticket;
    }

    public Page<SupportTicketDTO> getCustomerTickets(
            Long customerId,
            String status,
            String priority,
            String category,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            boolean includeClosed,
            Pageable pageable) {

        // In a real application, we would use JPA Specifications for complex queries
        // Specification<SupportTicket> spec = (root, query, cb) -> {
        //     List<Predicate> predicates = new ArrayList<>();
        //     predicates.add(cb.equal(root.get("customerId"), customerId));
        //     
        //     if (StringUtils.hasText(status)) {
        //         predicates.add(cb.equal(root.get("status"), status));
        //     }
        //     // Add other filters...
        //     
        //     return cb.and(predicates.toArray(new Predicate[0]));
        // };
        // 
        // return supportTicketRepository.findAll(spec, pageable)
        //     .map(ticket -> modelMapper.map(ticket, SupportTicketDTO.class));

        // For demo, filter in-memory data
        List<SupportTicketDTO> filteredTickets = ticketCache.values().stream()
                .filter(t -> t.getCustomerId().equals(customerId))
                .filter(t -> status == null || status.equals(t.getStatus()))
                .filter(t -> priority == null || priority.equals(t.getPriority()))
                .filter(t -> category == null || category.equals(t.getCategory()))
                .filter(t -> fromDate == null || !t.getCreatedAt().isBefore(fromDate))
                .filter(t -> toDate == null || !t.getCreatedAt().isAfter(toDate))
                .filter(t -> includeClosed || !"CLOSED".equals(t.getStatus()))
                .sorted((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt())) // Newest first
                .collect(Collectors.toList());

        // Simple pagination for demo (in a real app, use Pageable)
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredTickets.size());

        return new org.springframework.data.domain.PageImpl<>(
                filteredTickets.subList(start, end),
                pageable,
                filteredTickets.size()
        );
    }

    public SupportTicketDTO updateTicket(Long ticketId, SupportTicketDTO updateDTO) {
        // Get existing ticket or throw if not found
        SupportTicketDTO existingTicket = getTicket(ticketId, false, false);

        // Update all fields from the DTO
        updateDTO.setId(ticketId);
        updateDTO.setCustomerId(existingTicket.getCustomerId()); // Prevent changing customer ID
        updateDTO.setTicketNumber(existingTicket.getTicketNumber()); // Prevent changing ticket number
        updateDTO.setCreatedAt(existingTicket.getCreatedAt()); // Preserve creation date
        updateDTO.setUpdatedAt(LocalDateTime.now());

        // In a real application, we would save to the database
        // SupportTicket ticket = modelMapper.map(updateDTO, SupportTicket.class);
        // ticket = supportTicketRepository.save(ticket);

        // For demo, update in cache
        ticketCache.put(ticketId, updateDTO);

        return updateDTO;
    }

    public SupportTicketDTO partiallyUpdateTicket(Long ticketId, SupportTicketDTO updateDTO) {
        // Get existing ticket or throw if not found
        SupportTicketDTO existingTicket = getTicket(ticketId, false, false);

        // Only update non-null fields from the DTO
        if (updateDTO.getTitle() != null) {
            existingTicket.setTitle(updateDTO.getTitle());
        }
        if (updateDTO.getDescription() != null) {
            existingTicket.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getStatus() != null) {
            existingTicket.setStatus(updateDTO.getStatus());
        }
        if (updateDTO.getPriority() != null) {
            existingTicket.setPriority(updateDTO.getPriority());
        }
        if (updateDTO.getCategory() != null) {
            existingTicket.setCategory(updateDTO.getCategory());
        }
        if (updateDTO.getAssignedTo() != null) {
            existingTicket.setAssignedTo(updateDTO.getAssignedTo());
        }
        if (updateDTO.getTags() != null) {
            existingTicket.setTags(updateDTO.getTags());
        }

        // Always update the updatedAt timestamp
        existingTicket.setUpdatedAt(LocalDateTime.now());

        // In a real application, we would save to the database
        // SupportTicket ticket = modelMapper.map(existingTicket, SupportTicket.class);
        // ticket = supportTicketRepository.save(ticket);

        // For demo, update in cache
        ticketCache.put(ticketId, existingTicket);

        return existingTicket;
    }

    public TicketCommentDTO addComment(TicketCommentDTO commentDTO) {
        // Validate that the ticket exists
        getTicket(commentDTO.getTicketId(), false, false);


        // Set timestamps
        LocalDateTime now = LocalDateTime.now();
        commentDTO.setCreatedAt(now);
        commentDTO.setUpdatedAt(now);

        // In a real application, we would save to the database
        // TicketComment comment = modelMapper.map(commentDTO, TicketComment.class);
        // comment = ticketCommentRepository.save(comment);

        // For demo, generate an ID and store in cache
        commentDTO.setId(commentIdGenerator.incrementAndGet());
        commentCache.put(commentDTO.getId(), commentDTO);

        return commentDTO;
    }

    public Page<TicketCommentDTO> getTicketComments(
            Long ticketId,
            Boolean includeInternal,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            Pageable pageable) {

        // In a real application, we would fetch from the database with pagination
        // Specification<TicketComment> spec = (root, query, cb) -> {
        //     List<Predicate> predicates = new ArrayList<>();
        //     predicates.add(cb.equal(root.get("ticketId"), ticketId));
        //     
        //     if (includeInternal != null && !includeInternal) {
        //         predicates.add(cb.isFalse(root.get("isInternal")));
        //     }
        //     
        //     if (fromDate != null) {
        //         predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
        //     }
        //     
        //     if (toDate != null) {
        //         predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), toDate));
        //     }
        //     
        //     return cb.and(predicates.toArray(new Predicate[0]));
        // };
        // 
        // return ticketCommentRepository.findAll(spec, pageable)
        //     .map(comment -> modelMapper.map(comment, TicketCommentDTO.class));

        // For demo, filter in-memory data
        List<TicketCommentDTO> filteredComments = commentCache.values().stream()
                .filter(c -> c.getTicketId().equals(ticketId))
                .filter(c -> includeInternal == null || includeInternal || !c.isInternal())
                .filter(c -> fromDate == null || !c.getCreatedAt().isBefore(fromDate))
                .filter(c -> toDate == null || !c.getCreatedAt().isAfter(toDate))
                .sorted((c1, c2) -> c1.getCreatedAt().compareTo(c2.getCreatedAt())) // Oldest first
                .collect(Collectors.toList());
        
        // Simple pagination for demo (in a real app, use Pageable)
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredComments.size());

        return new org.springframework.data.domain.PageImpl<>(
                filteredComments.subList(start, end),
                pageable,
                filteredComments.size()
        );
    }

    public TicketCommentDTO getTicketComment(Long ticketId, Long commentId) {
        // In a real application, we would fetch from the database
        // TicketComment comment = ticketCommentRepository.findByIdAndTicketId(commentId, ticketId)
        //     .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        // return modelMapper.map(comment, TicketCommentDTO.class);

        // For demo, get from cache or throw if not found
        TicketCommentDTO comment = commentCache.get(commentId);
        if (comment == null || !comment.getTicketId().equals(ticketId)) {
            throw new ResourceNotFoundException("Comment not found with id: " + commentId);
        }

        return comment;
    }

    public Page<FaqDTO> getFrequentlyAskedQuestions(
            String category,
            String query,
            boolean includeUnpublished,
            Pageable pageable) {

        // In a real application, we would fetch from the database with pagination and filtering
        // For demo, return a page with sample data
        List<FaqDTO> faqs = new ArrayList<>();

        FaqDTO faq1 = new FaqDTO();
        faq1.setId(1L);
        faq1.setQuestion("How do I reset my password?");
        faq1.setAnswer("Click on 'Forgot Password' on the login page and follow the instructions.");
        faq1.setCategory("Account");
        faq1.setStatus("PUBLISHED");
        faq1.setViewCount(42);
        faq1.setHelpfulCount(35);
        faq1.setNotHelpfulCount(2);
        faq1.setCreatedAt(LocalDateTime.now().minusMonths(1));
        faq1.setUpdatedAt(LocalDateTime.now().minusDays(5));
        faqs.add(faq1);

        FaqDTO faq2 = new FaqDTO();
        faq2.setId(2L);
        faq2.setQuestion("What should I do if I was charged incorrectly?");
        faq2.setAnswer("Please contact our support team immediately with details of the incorrect charge for investigation.");
        faq2.setCategory("PAYMENTS");
        faq2.setStatus("PUBLISHED");
        faq2.setViewCount(87);
        faq2.setHelpfulCount(72);
        faq2.setNotHelpfulCount(3);
        faq2.setCreatedAt(LocalDateTime.now().minusMonths(2));
        faq2.setUpdatedAt(LocalDateTime.now().minusDays(10));
        faqs.add(faq2);

        // Apply filters
        List<FaqDTO> filteredFaqs = faqs.stream()
                .filter(f -> includeUnpublished || "PUBLISHED".equals(f.getStatus()))
                .filter(f -> category == null || category.equalsIgnoreCase(f.getCategory()))
                .filter(f -> query == null ||
                        f.getQuestion().toLowerCase().contains(query.toLowerCase()) ||
                        f.getAnswer().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        // Simple pagination for demo (in a real app, use Pageable)
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredFaqs.size());

        return new org.springframework.data.domain.PageImpl<>(
                filteredFaqs.subList(start, end),
                pageable,
                filteredFaqs.size()
        );
    }

    public List<String> getFaqCategories() {
        // In a real application, we would fetch distinct categories from the database
        // For demo, return a hardcoded list
        return List.of("Account", "PAYMENTS", "ORDERS", "SHIPPING");
    }

    public void recordFaqFeedback(Long faqId, boolean wasHelpful) {
        // In a real application, we would update the FAQ's helpful/not helpful counts
        // For demo, just log the feedback
        System.out.println("FAQ feedback recorded - FAQ ID: " + faqId + ", Was helpful: " + wasHelpful);
    }

    private void initializeSampleData() {
        // Sample support tickets
        SupportTicketDTO ticket1 = new SupportTicketDTO();
        ticket1.setId(1001L);
        ticket1.setTicketNumber("TKT-2023-1001");
        ticket1.setCustomerId(12345L);
        ticket1.setTitle("Payment issue with order #12345");
        ticket1.setDescription("I was charged twice for my order. Please help!");
        ticket1.setStatus("OPEN");
        ticket1.setPriority("HIGH");
        ticket1.setCategory("PAYMENT");
        ticket1.setCreatedAt(LocalDateTime.now().minusDays(2));
        ticket1.setUpdatedAt(LocalDateTime.now().minusHours(2));
        ticketCache.put(ticket1.getId(), ticket1);

        // Sample ticket comments
        TicketCommentDTO comment1 = new TicketCommentDTO();
        comment1.setId(10001L);
        comment1.setTicketId(1001L);
        comment1.setAuthorId(12345L);
        comment1.setAuthorType("CUSTOMER");
        comment1.setAuthorName("John Doe");
        comment1.setAuthorEmail("john.doe@example.com");
        comment1.setComment("I noticed a double charge on my credit card for order #12345.");
        comment1.setInternal(false);
        comment1.setStatus("VISIBLE");
        comment1.setCreatedAt(LocalDateTime.now().minusDays(2));
        comment1.setUpdatedAt(LocalDateTime.now().minusDays(2));
        comment1.setUpdatedBy("system");
        comment1.setIpAddress("127.0.0.1");
        comment1.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
        commentCache.put(comment1.getId(), comment1);

        // Sample FAQs are already added in the getFrequentlyAskedQuestions method
    }
    
    /**
     * Updates the status of a support ticket
     *
     * @param ticketId The ID of the ticket to update
     * @param status The new status to set
     * @param resolutionSummary Optional summary when resolving/closing the ticket
     * @return The updated ticket DTO
     * @throws ResourceNotFoundException if the ticket is not found
     */
    public SupportTicketDTO updateTicketStatus(Long ticketId, String status, String resolutionSummary) {
        // Get existing ticket or throw if not found
        SupportTicketDTO existingTicket = getTicket(ticketId, false, false);
        
        // Update status and resolution summary if provided
        existingTicket.setStatus(status);
        
        // If this is a resolution (e.g., CLOSED, RESOLVED), set the resolution summary
        if ((status.equalsIgnoreCase("CLOSED") || status.equalsIgnoreCase("RESOLVED")) 
                && resolutionSummary != null && !resolutionSummary.trim().isEmpty()) {
            existingTicket.setResolutionSummary(resolutionSummary);
        }
        
        // Update the last updated timestamp
        existingTicket.setUpdatedAt(LocalDateTime.now());
        
        // In a real application, we would save to the database
        // SupportTicket ticket = modelMapper.map(existingTicket, SupportTicket.class);
        // ticket = supportTicketRepository.save(ticket);
        
        // For demo, update in cache
        ticketCache.put(ticketId, existingTicket);
        
        return existingTicket;
    }
    
    /**
     * Assigns a support ticket to a user
     *
     * @param ticketId The ID of the ticket to assign
     * @param assigneeId The ID of the user to assign the ticket to
     * @return The updated ticket DTO
     * @throws ResourceNotFoundException if the ticket is not found
     */
    public SupportTicketDTO assignTicket(Long ticketId, String assigneeId) {
        // Get existing ticket or throw if not found
        SupportTicketDTO existingTicket = getTicket(ticketId, false, false);
        
        // Update assignee and timestamps
        existingTicket.setAssignedTo(assigneeId);
        //existingTicket.setAssignedAt(LocalDateTime.now());
        existingTicket.setUpdatedAt(LocalDateTime.now());
        
        // In a real application, we would save to the database
        // SupportTicket ticket = modelMapper.map(existingTicket, SupportTicket.class);
        // ticket = supportTicketRepository.save(ticket);
        
        // For demo, update in cache
        ticketCache.put(ticketId, existingTicket);
        
        return existingTicket;
    }
    
    /**
     * Updates the priority of a support ticket
     *
     * @param ticketId The ID of the ticket to update
     * @param priority The new priority to set (e.g., "LOW", "MEDIUM", "HIGH", "URGENT")
     * @return The updated ticket DTO
     * @throws ResourceNotFoundException if the ticket is not found
     * @throws IllegalArgumentException if the priority is invalid
     */
    public SupportTicketDTO updateTicketPriority(Long ticketId, String priority) {
        // Validate priority
        if (priority == null || priority.trim().isEmpty()) {
            throw new IllegalArgumentException("Priority cannot be null or empty");
        }
        
        // Get existing ticket or throw if not found
        SupportTicketDTO existingTicket = getTicket(ticketId, false, false);
        
        // Update priority and timestamps
        existingTicket.setPriority(priority.toUpperCase());
        existingTicket.setUpdatedAt(LocalDateTime.now());
        
        // In a real application, we would save to the database
        // SupportTicket ticket = modelMapper.map(existingTicket, SupportTicket.class);
        // ticket = supportTicketRepository.save(ticket);
        
        // For demo, update in cache
        ticketCache.put(ticketId, existingTicket);
        
        return existingTicket;
    }
    
    /**
     * Retrieves statistics for the support dashboard
     *
     * @param startDate The start date for filtering statistics (inclusive)
     * @param endDate The end date for filtering statistics (exclusive)
     * @return A map containing various dashboard statistics
     */
    public Map<String, Object> getDashboardStats(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> stats = new HashMap<>();
        
        // Get all tickets, filtered by date range if provided
        List<SupportTicketDTO> allTickets = new ArrayList<>(ticketCache.values());
        
        // Filter tickets by date range if dates are provided
        if (startDate != null || endDate != null) {
            allTickets = allTickets.stream()
                .filter(ticket -> {
                    boolean afterStart = startDate == null || !ticket.getCreatedAt().isBefore(startDate);
                    boolean beforeEnd = endDate == null || ticket.getCreatedAt().isBefore(endDate);
                    return afterStart && beforeEnd;
                })
                .collect(Collectors.toList());
        }
        
        // Calculate basic statistics
        long totalTickets = allTickets.size();
        long openTickets = allTickets.stream().filter(t -> "OPEN".equals(t.getStatus())).count();
        long inProgressTickets = allTickets.stream().filter(t -> "IN_PROGRESS".equals(t.getStatus())).count();
        long resolvedTickets = allTickets.stream().filter(t -> "RESOLVED".equals(t.getStatus())).count();
        long closedTickets = allTickets.stream().filter(t -> "CLOSED".equals(t.getStatus())).count();
        
        // Calculate tickets by priority
        Map<String, Long> ticketsByPriority = allTickets.stream()
            .collect(Collectors.groupingBy(SupportTicketDTO::getPriority, Collectors.counting()));
        
        // Calculate tickets by category
        Map<String, Long> ticketsByCategory = allTickets.stream()
            .collect(Collectors.groupingBy(SupportTicketDTO::getCategory, Collectors.counting()));
        
        // Calculate average resolution time (in hours)
        double avgResolutionHours = allTickets.stream()
            .filter(t -> t.getResolvedAt() != null && t.getCreatedAt() != null)
            .mapToLong(t -> Duration.between(t.getCreatedAt(), t.getResolvedAt()).toHours())
            .average()
            .orElse(0.0);
        
        // Populate the stats map
        stats.put("totalTickets", totalTickets);
        stats.put("openTickets", openTickets);
        stats.put("inProgressTickets", inProgressTickets);
        stats.put("resolvedTickets", resolvedTickets);
        stats.put("closedTickets", closedTickets);
        stats.put("ticketsByPriority", ticketsByPriority);
        stats.put("ticketsByCategory", ticketsByCategory);
        stats.put("averageResolutionHours", Math.round(avgResolutionHours * 100.0) / 100.0);
        
        return stats;
    }
    
    /**
     * Searches for tickets based on various criteria with pagination
     *
     * @param query The search query to match against ticket title or description
     * @param status The status to filter by (e.g., "OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED")
     * @param priority The priority to filter by (e.g., "LOW", "MEDIUM", "HIGH", "URGENT")
     * @param category The category to filter by
     * @param fromDate The start date for filtering by creation date (inclusive)
     * @param toDate The end date for filtering by creation date (exclusive)
     * @param pageable The pagination information
     * @return A page of SupportTicketDTO matching the search criteria
     */
    public Page<SupportTicketDTO> searchTickets(
            String query,
            String status,
            String priority,
            String category,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            Pageable pageable) {
            
        // Start with all tickets
        List<SupportTicketDTO> allTickets = new ArrayList<>(ticketCache.values());
        
        // Apply filters
        Stream<SupportTicketDTO> ticketStream = allTickets.stream();
        
        // Filter by search query (title or description)
        if (query != null && !query.trim().isEmpty()) {
            String lowerQuery = query.toLowerCase();
            ticketStream = ticketStream.filter(ticket -> 
                (ticket.getTitle() != null && ticket.getTitle().toLowerCase().contains(lowerQuery)) ||
                (ticket.getDescription() != null && ticket.getDescription().toLowerCase().contains(lowerQuery))
            );
        }
        
        // Filter by status
        if (status != null && !status.trim().isEmpty()) {
            ticketStream = ticketStream.filter(ticket -> status.equalsIgnoreCase(ticket.getStatus()));
        }
        
        // Filter by priority
        if (priority != null && !priority.trim().isEmpty()) {
            ticketStream = ticketStream.filter(ticket -> priority.equalsIgnoreCase(ticket.getPriority()));
        }
        
        // Filter by category
        if (category != null && !category.trim().isEmpty()) {
            ticketStream = ticketStream.filter(ticket -> category.equalsIgnoreCase(ticket.getCategory()));
        }
        
        // Filter by date range
        if (fromDate != null) {
            ticketStream = ticketStream.filter(ticket -> 
                ticket.getCreatedAt() != null && !ticket.getCreatedAt().isBefore(fromDate)
            );
        }
        if (toDate != null) {
            ticketStream = ticketStream.filter(ticket -> 
                ticket.getCreatedAt() != null && ticket.getCreatedAt().isBefore(toDate)
            );
        }
        
        // Apply sorting and pagination
        List<SupportTicketDTO> filteredTickets = ticketStream.collect(Collectors.toList());
        
        // Apply pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredTickets.size());
        
        // Handle case where start is greater than list size
        if (start > filteredTickets.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, filteredTickets.size());
        }
        
        List<SupportTicketDTO> pageContent = filteredTickets.subList(start, end);
        
        return new PageImpl<>(pageContent, pageable, filteredTickets.size());
    }
}
