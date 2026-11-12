package com.nexodesk.ticket.entity;

import java.time.Instant;
import java.util.UUID;

import com.nexodesk.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticket_comments")
public class TicketComment {

    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    @Column(nullable = false, columnDefinition = "text")
    private String content;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected TicketComment() {
    }
    public TicketComment(Ticket ticket, User author, String content) {
        this.ticket = ticket;
        this.author = author;
        this.content = content;
    }
    @PrePersist
    void onCreate() {
        if (id == null) id = UUID.randomUUID();
        createdAt = Instant.now();
    }
    public UUID getId() { return id; }
    public User getAuthor() { return author; }
    public String getContent() { return content; }
    public Instant getCreatedAt() { return createdAt; }
}
