package com.nexodesk.ticket.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexodesk.common.exception.ResourceNotFoundException;
import com.nexodesk.ticket.dto.AssignTicketRequest;
import com.nexodesk.ticket.dto.CommentRequest;
import com.nexodesk.ticket.dto.CommentResponse;
import com.nexodesk.ticket.dto.CreateTicketRequest;
import com.nexodesk.ticket.dto.HistoryResponse;
import com.nexodesk.ticket.dto.TicketResponse;
import com.nexodesk.ticket.dto.UpdateTicketRequest;
import com.nexodesk.ticket.entity.Ticket;
import com.nexodesk.ticket.entity.TicketComment;
import com.nexodesk.ticket.entity.TicketHistory;
import com.nexodesk.ticket.entity.TicketStatus;
import com.nexodesk.ticket.repository.TicketCommentRepository;
import com.nexodesk.ticket.repository.TicketHistoryRepository;
import com.nexodesk.ticket.repository.TicketRepository;
import com.nexodesk.user.entity.Role;
import com.nexodesk.user.entity.User;
import com.nexodesk.user.repository.UserRepository;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketCommentRepository commentRepository;
    private final TicketHistoryRepository historyRepository;
    private final UserRepository userRepository;

    public TicketService(
        TicketRepository ticketRepository,
        TicketCommentRepository commentRepository,
        TicketHistoryRepository historyRepository,
        UserRepository userRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.commentRepository = commentRepository;
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TicketResponse create(CreateTicketRequest request, Authentication authentication) {
        User requester = currentUser(authentication);
        return TicketResponse.from(ticketRepository.save(
            new Ticket(request.title(), request.description(), request.priority(), requester)
        ));
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> list(Authentication authentication) {
        User user = currentUser(authentication);
        List<Ticket> tickets = isPrivileged(user)
            ? ticketRepository.findAllByOrderByCreatedAtDesc()
            : ticketRepository.findByRequesterIdOrderByCreatedAtDesc(user.getId());
        return tickets.stream().map(TicketResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public TicketResponse get(UUID id, Authentication authentication) {
        Ticket ticket = find(id);
        assertCanView(ticket, currentUser(authentication));
        return TicketResponse.from(ticket);
    }

    @Transactional
    public TicketResponse update(UUID id, UpdateTicketRequest request, Authentication authentication) {
        User actor = currentUser(authentication);
        Ticket ticket = find(id);
        assertCanManage(ticket, actor);
        record(ticket, actor, "title", ticket.getTitle(), request.title());
        record(ticket, actor, "description", ticket.getDescription(), request.description());
        record(ticket, actor, "priority", ticket.getPriority().name(), request.priority().name());
        record(ticket, actor, "status", ticket.getStatus().name(), request.status().name());
        ticket.update(request.title(), request.description(), request.priority());
        ticket.changeStatus(request.status());
        return TicketResponse.from(ticketRepository.save(ticket));
    }

    @Transactional
    public TicketResponse assign(UUID id, AssignTicketRequest request, Authentication authentication) {
        User actor = currentUser(authentication);
        requireRole(actor, Role.ADMIN, Role.TECHNICIAN);
        Ticket ticket = find(id);
        User technician = userRepository.findById(request.technicianId())
            .orElseThrow(() -> new ResourceNotFoundException("Technician not found"));
        requireRole(technician, Role.TECHNICIAN, Role.ADMIN);
        record(ticket, actor, "assignedTechnician", ticket.getAssignedTechnician() == null
            ? null : ticket.getAssignedTechnician().getId().toString(), technician.getId().toString());
        ticket.assignTo(technician);
        if (ticket.getStatus() == TicketStatus.OPEN) {
            record(ticket, actor, "status", ticket.getStatus().name(), TicketStatus.IN_PROGRESS.name());
            ticket.changeStatus(TicketStatus.IN_PROGRESS);
        }
        return TicketResponse.from(ticketRepository.save(ticket));
    }

    @Transactional
    public CommentResponse addComment(UUID id, CommentRequest request, Authentication authentication) {
        User actor = currentUser(authentication);
        Ticket ticket = find(id);
        assertCanView(ticket, actor);
        return CommentResponse.from(commentRepository.save(new TicketComment(ticket, actor, request.content())));
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> comments(UUID id, Authentication authentication) {
        Ticket ticket = find(id);
        assertCanView(ticket, currentUser(authentication));
        return commentRepository.findByTicketIdOrderByCreatedAtAsc(id).stream().map(CommentResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<HistoryResponse> history(UUID id, Authentication authentication) {
        Ticket ticket = find(id);
        assertCanManage(ticket, currentUser(authentication));
        return historyRepository.findByTicketIdOrderByCreatedAtAsc(id).stream().map(HistoryResponse::from).toList();
    }

    private void record(Ticket ticket, User actor, String field, String oldValue, String newValue) {
        if (!java.util.Objects.equals(oldValue, newValue)) {
            historyRepository.save(new TicketHistory(ticket, actor, field, oldValue, newValue));
        }
    }

    private Ticket find(UUID id) {
        return ticketRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
    }

    private User currentUser(Authentication authentication) {
        return userRepository.findByEmailIgnoreCase(authentication.getName())
            .filter(User::isActive)
            .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

    private boolean isPrivileged(User user) {
        return user.getRole() == Role.ADMIN || user.getRole() == Role.TECHNICIAN;
    }

    private void assertCanView(Ticket ticket, User user) {
        if (!isPrivileged(user) && !ticket.getRequester().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot access this ticket");
        }
    }

    private void assertCanManage(Ticket ticket, User user) {
        if (user.getRole() == Role.ADMIN) return;
        if (user.getRole() == Role.TECHNICIAN
            && (ticket.getAssignedTechnician() == null
                || ticket.getAssignedTechnician().getId().equals(user.getId()))) return;
        if (user.getRole() == Role.EMPLOYEE
            && ticket.getRequester().getId().equals(user.getId())
            && ticket.getStatus() != TicketStatus.CLOSED) return;
        throw new AccessDeniedException("You cannot modify this ticket");
    }

    private void requireRole(User user, Role... roles) {
        for (Role role : roles) {
            if (user.getRole() == role) return;
        }
        throw new AccessDeniedException("Insufficient role");
    }
}
