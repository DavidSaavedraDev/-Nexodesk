package com.nexodesk.ticket.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexodesk.ticket.entity.TicketComment;

public interface TicketCommentRepository extends JpaRepository<TicketComment, UUID> {
    List<TicketComment> findByTicketIdOrderByCreatedAtAsc(UUID ticketId);
}
