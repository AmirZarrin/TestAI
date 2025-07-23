package com.ai.testai.repository;

import com.ai.testai.entity.TicketComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketCommentRepository extends JpaRepository<TicketComment, Long> {
    // Find all comments for a specific ticket
    List<TicketComment> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
    
    // Find all comments for a specific ticket, including internal ones
    List<TicketComment> findByTicketIdAndIsInternalOrderByCreatedAtAsc(Long ticketId, Boolean isInternal);
    
    // Count comments for a specific ticket
    Long countByTicketId(Long ticketId);
}
