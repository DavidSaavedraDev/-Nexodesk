package com.nexodesk.ticket.entity;

import java.time.Instant;
import java.util.UUID;

import com.nexodesk.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, updatable = false)
    private String ticketNumber;

    @Column(nullable = false, length = 160)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_technician_id")
    private User assignedTechnician;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected Ticket() {
    }

    public Ticket(String title, String description, TicketPriority priority, User requester) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = TicketStatus.OPEN;
        this.requester = requester;
    }

    @PrePersist
    void onCreate() {
        if (id == null) id = UUID.randomUUID();
        if (ticketNumber == null) ticketNumber = "NEXO-" + id.toString().substring(0, 8).toUpperCase();
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getTicketNumber() { return ticketNumber; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public TicketPriority getPriority() { return priority; }
    public TicketStatus getStatus() { return status; }
    public User getRequester() { return requester; }
    public User getAssignedTechnician() { return assignedTechnician; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void update(String title, String description, TicketPriority priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }
    public void changeStatus(TicketStatus status) { this.status = status; }
    public void assignTo(User technician) { this.assignedTechnician = technician; }
}
