package com.helpdesk.system.service;

import com.helpdesk.system.dto.TicketCreationDTO;
import com.helpdesk.system.dto.TicketDTO;
import com.helpdesk.system.model.Status;

import java.util.List;

public interface TicketService {
    TicketDTO createTicket(TicketCreationDTO ticketCreationDTO);
    TicketDTO assignTicket(Long ticketId, Long agentId);
    TicketDTO updateTicketStatus(Long ticketId, Status status);
    TicketDTO getTicketById(Long ticketId);
    List<TicketDTO> getAllTickets();

    List<TicketDTO> getTicketsByStatus(Status status);
    List<TicketDTO> getTicketsAssignedTo(Long agentId);
}
