package com.nexodesk.ticket.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.nexodesk.ticket.dto.CreateTicketRequest;
import com.nexodesk.ticket.entity.Ticket;
import com.nexodesk.ticket.entity.TicketPriority;
import com.nexodesk.ticket.repository.TicketCommentRepository;
import com.nexodesk.ticket.repository.TicketHistoryRepository;
import com.nexodesk.ticket.repository.TicketRepository;
import com.nexodesk.user.entity.Role;
import com.nexodesk.user.entity.User;
import com.nexodesk.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock TicketRepository ticketRepository;
    @Mock TicketCommentRepository commentRepository;
    @Mock TicketHistoryRepository historyRepository;
    @Mock UserRepository userRepository;

    private TicketService service;
    private User employee;

    @BeforeEach
    void setUp() {
        service = new TicketService(ticketRepository, commentRepository, historyRepository, userRepository);
        employee = new User("Test", "Employee", "employee@example.com", "hash", Role.EMPLOYEE);
    }

    @Test
    void createsTicketForAuthenticatedEmployee() {
        var authentication = new UsernamePasswordAuthenticationToken(employee.getEmail(), null);
        when(userRepository.findByEmailIgnoreCase(employee.getEmail())).thenReturn(java.util.Optional.of(employee));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.create(
            new CreateTicketRequest("Cannot connect", "VPN is unavailable", TicketPriority.HIGH),
            authentication
        );

        assertEquals(TicketPriority.HIGH, response.priority());
        assertEquals(employee.getId(), response.requesterId());
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void rejectsAccessToAnotherEmployeesTicket() {
        User other = new User("Other", "Employee", "other@example.com", "hash", Role.EMPLOYEE);
        Ticket ticket = new Ticket("Title", "Description", TicketPriority.LOW, other);
        UUID id = ticket.getId();
        when(ticketRepository.findById(id)).thenReturn(java.util.Optional.of(ticket));
        when(userRepository.findByEmailIgnoreCase(employee.getEmail())).thenReturn(java.util.Optional.of(employee));

        var authentication = new UsernamePasswordAuthenticationToken(employee.getEmail(), null);

        assertThrows(AccessDeniedException.class, () -> service.get(id, authentication));
    }

    @Test
    void rejectsUpdateOfAnotherEmployeesTicket() {
        User other = new User("Other", "Employee", "other@example.com", "hash", Role.EMPLOYEE);
        Ticket ticket = new Ticket("Title", "Description", TicketPriority.LOW, other);
        UUID id = ticket.getId();
        when(ticketRepository.findById(id)).thenReturn(java.util.Optional.of(ticket));
        when(userRepository.findByEmailIgnoreCase(employee.getEmail())).thenReturn(java.util.Optional.of(employee));

        var authentication = new UsernamePasswordAuthenticationToken(employee.getEmail(), null);

        assertThrows(AccessDeniedException.class, () -> service.update(
            id,
            new com.nexodesk.ticket.dto.UpdateTicketRequest(
                "Changed", "Changed description", TicketPriority.HIGH,
                com.nexodesk.ticket.entity.TicketStatus.OPEN
            ),
            authentication
        ));
    }
}
