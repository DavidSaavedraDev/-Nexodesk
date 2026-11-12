package com.nexodesk.ticket.dto;

import com.nexodesk.ticket.entity.TicketPriority;
import com.nexodesk.ticket.entity.TicketStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateTicketRequest(
    @NotBlank @Size(max = 160) String title,
    @NotBlank String description,
    @NotNull TicketPriority priority,
    @NotNull TicketStatus status
) {
}
