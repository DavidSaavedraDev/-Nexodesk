package com.nexodesk.ticket.dto;

import java.time.Instant;
import java.util.UUID;

import com.nexodesk.ticket.entity.Ticket;
import com.nexodesk.ticket.entity.TicketPriority;
import com.nexodesk.ticket.entity.TicketStatus;

public record TicketResponse(
    UUID id,
    String ticketNumber,
    String title,
    String description,
    TicketPriority priority,
    TicketStatus status,
    UUID requesterId,
    UUID assignedTechnicianId,
    Instant createdAt,
    Instant updatedAt
) {
    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(
            ticket.getId(), ticket.getTicketNumber(), ticket.getTitle(), ticket.getDescription(),
            ticket.getPriority(), ticket.getStatus(), ticket.getRequester().getId(),
            ticket.getAssignedTechnician() == null ? null : ticket.getAssignedTechnician().getId(),
            ticket.getCreatedAt(), ticket.getUpdatedAt()
        );
    }
}
