package com.bnplcore.repository;

import com.bnplcore.entity.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    // Custom query methods can be added here if needed
}
