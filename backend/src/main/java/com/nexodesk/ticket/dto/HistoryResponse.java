package com.nexodesk.ticket.dto;

import java.time.Instant;
import java.util.UUID;

import com.nexodesk.ticket.entity.TicketHistory;

public record HistoryResponse(
    UUID id,
    UUID changedById,
    String fieldName,
    String oldValue,
    String newValue,
    Instant createdAt
) {
    public static HistoryResponse from(TicketHistory history) {
        return new HistoryResponse(
            history.getId(), history.getChangedById(), history.getFieldName(),
            history.getOldValue(), history.getNewValue(), history.getCreatedAt()
        );
    }
}
