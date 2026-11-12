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
@Table(name = "ticket_history")
public class TicketHistory {

    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "changed_by", nullable = false)
    private User changedBy;
    @Column(nullable = false, length = 80)
    private String fieldName;
    @Column(columnDefinition = "text")
    private String oldValue;
    @Column(columnDefinition = "text")
    private String newValue;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected TicketHistory() {
    }
    public TicketHistory(Ticket ticket, User changedBy, String fieldName, String oldValue, String newValue) {
        this.ticket = ticket;
        this.changedBy = changedBy;
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    @PrePersist
    void onCreate() {
        if (id == null) id = UUID.randomUUID();
        createdAt = Instant.now();
    }
    public UUID getId() { return id; }
    public UUID getChangedById() { return changedBy.getId(); }
    public String getFieldName() { return fieldName; }
    public String getOldValue() { return oldValue; }
    public String getNewValue() { return newValue; }
    public Instant getCreatedAt() { return createdAt; }
}
