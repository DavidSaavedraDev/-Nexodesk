package com.nexodesk.ticket.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexodesk.ticket.entity.TicketHistory;

public interface TicketHistoryRepository extends JpaRepository<TicketHistory, UUID> {
    List<TicketHistory> findByTicketIdOrderByCreatedAtAsc(UUID ticketId);
}
