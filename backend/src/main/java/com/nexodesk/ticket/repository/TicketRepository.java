package com.nexodesk.ticket.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexodesk.ticket.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findAllByOrderByCreatedAtDesc();
    List<Ticket> findByRequesterIdOrderByCreatedAtDesc(UUID requesterId);
}
