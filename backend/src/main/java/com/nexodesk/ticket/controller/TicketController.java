package com.nexodesk.ticket.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nexodesk.ticket.dto.AssignTicketRequest;
import com.nexodesk.ticket.dto.CommentRequest;
import com.nexodesk.ticket.dto.CommentResponse;
import com.nexodesk.ticket.dto.CreateTicketRequest;
import com.nexodesk.ticket.dto.HistoryResponse;
import com.nexodesk.ticket.dto.TicketResponse;
import com.nexodesk.ticket.dto.UpdateTicketRequest;
import com.nexodesk.ticket.service.TicketService;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse create(@Valid @RequestBody CreateTicketRequest request, Authentication authentication) {
        return ticketService.create(request, authentication);
    }

    @GetMapping
    public List<TicketResponse> list(Authentication authentication) {
        return ticketService.list(authentication);
    }

    @GetMapping("/{id}")
    public TicketResponse get(@PathVariable UUID id, Authentication authentication) {
        return ticketService.get(id, authentication);
    }

    @PutMapping("/{id}")
    public TicketResponse update(
        @PathVariable UUID id,
        @Valid @RequestBody UpdateTicketRequest request,
        Authentication authentication
    ) {
        return ticketService.update(id, request, authentication);
    }

    @PostMapping("/{id}/assign")
    public TicketResponse assign(
        @PathVariable UUID id,
        @Valid @RequestBody AssignTicketRequest request,
        Authentication authentication
    ) {
        return ticketService.assign(id, request, authentication);
    }

    @PostMapping("/{id}/comments")
    public CommentResponse addComment(
        @PathVariable UUID id,
        @Valid @RequestBody CommentRequest request,
        Authentication authentication
    ) {
        return ticketService.addComment(id, request, authentication);
    }

    @GetMapping("/{id}/comments")
    public List<CommentResponse> comments(@PathVariable UUID id, Authentication authentication) {
        return ticketService.comments(id, authentication);
    }

    @GetMapping("/{id}/history")
    public List<HistoryResponse> history(@PathVariable UUID id, Authentication authentication) {
        return ticketService.history(id, authentication);
    }
}
