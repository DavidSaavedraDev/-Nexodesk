package com.nexodesk.ticket.dto;

import java.time.Instant;
import java.util.UUID;

import com.nexodesk.ticket.entity.TicketComment;

public record CommentResponse(UUID id, UUID authorId, String content, Instant createdAt) {
    public static CommentResponse from(TicketComment comment) {
        return new CommentResponse(comment.getId(), comment.getAuthor().getId(), comment.getContent(), comment.getCreatedAt());
    }
}
