package com.helpdesk.system.controller;


import com.helpdesk.system.dto.AssignAgentDTO;
import com.helpdesk.system.dto.TicketCreationDTO;
import com.helpdesk.system.dto.TicketDTO;
import com.helpdesk.system.dto.UpdateStatusDTO;
import com.helpdesk.system.model.Status;
import com.helpdesk.system.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService){
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getTickets(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Long agentId
    ) {
        List<TicketDTO> tickets;
        if (status != null) {
            tickets = ticketService.getTicketsByStatus(status);
        } else if (agentId != null) {
            tickets = ticketService.getTicketsAssignedTo(agentId);
        } else {
            tickets = ticketService.getAllTickets();
        }
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id){
        TicketDTO ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticket);
    }
    
    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketCreationDTO ticketData){
        TicketDTO createdTicket = ticketService.createTicket(ticketData);
        return ResponseEntity.status(201).body(createdTicket);
    }
    
    @PatchMapping("{id}/status")
    public ResponseEntity<TicketDTO> updateTicketStatus(@PathVariable Long id, @RequestBody UpdateStatusDTO statusUpdated){
        TicketDTO updatedTicket = ticketService.updateTicketStatus(id, statusUpdated.getNewStatus());
        return ResponseEntity.ok(updatedTicket);
    }

    @PatchMapping("{id}/assign")
    public ResponseEntity<TicketDTO> assignAgentToTicket(@PathVariable Long id, @RequestBody AssignAgentDTO assignee){
        TicketDTO updatedTicket = ticketService.assignTicket(id, assignee.getAgentId());
        return ResponseEntity.ok(updatedTicket);
    }

}
