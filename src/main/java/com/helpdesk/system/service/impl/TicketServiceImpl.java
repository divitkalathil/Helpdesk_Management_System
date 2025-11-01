package com.helpdesk.system.service.impl;

import com.helpdesk.system.dto.TicketCreationDTO;
import com.helpdesk.system.dto.TicketDTO;
import com.helpdesk.system.exception.ResourceNotFoundException;
import com.helpdesk.system.model.Status;
import com.helpdesk.system.model.Ticket;
import com.helpdesk.system.model.User;
import com.helpdesk.system.repository.TicketRepository;
import com.helpdesk.system.repository.UserRepository;
import com.helpdesk.system.service.TicketService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public TicketServiceImpl(TicketRepository ticketRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        configureModelMapper();
    }

    private void configureModelMapper() {
        // Custom mapping for nested properties and different field names
        modelMapper.typeMap(Ticket.class, TicketDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getCreatedBy().getUsername(), TicketDTO::setCreatedByUsername);
            // Use a condition to handle null assignedTo user
            mapper.map(src -> src.getAssignedTo() != null ? src.getAssignedTo().getUsername() : null, TicketDTO::setAssignedToUsername);
            mapper.map(Ticket::getCreatedAt, TicketDTO::setCreatedDate);
            mapper.map(Ticket::getUpdatedAt, TicketDTO::setUpdatedDate);
        });
    }

    @Override
    public TicketDTO getTicketById(Long ticketId){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()-> new ResourceNotFoundException("Ticket not found with ID: " + ticketId));
        return modelMapper.map(ticket, TicketDTO.class);
    }

    @Override
    public List<TicketDTO> getAllTickets(){
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream()
                .map(ticket-> modelMapper.map(ticket, TicketDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public TicketDTO createTicket(TicketCreationDTO ticketCreationDTO){
        User createdBy = userRepository.findById(ticketCreationDTO.getCreatedById())
                .orElseThrow(()->new ResourceNotFoundException("User not found with ID: " + ticketCreationDTO.getCreatedById()));
        Ticket newTicket = new Ticket();
        newTicket.setTitle(ticketCreationDTO.getTitle());
        newTicket.setDescription(ticketCreationDTO.getDescription());
        newTicket.setPriority(ticketCreationDTO.getPriority());

        newTicket.setCreatedBy(createdBy);
        newTicket.setStatus(Status.OPEN);
        newTicket.setCreatedAt(LocalDateTime.now());

        Ticket savedTicket = ticketRepository.save(newTicket);

        return modelMapper.map(savedTicket, TicketDTO.class);
    }


    @Override
    public TicketDTO assignTicket(Long ticketId, Long agentId){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()-> new ResourceNotFoundException("Ticket not found with ID: " + ticketId));
        User agent = userRepository.findById(agentId)
                .orElseThrow(()-> new ResourceNotFoundException("Agent not found with ID: " + agentId));

        ticket.setAssignedTo(agent);
        ticket.setStatus(Status.IN_PROGRESS);
        ticket.setUpdatedAt(LocalDateTime.now());

        Ticket updatedTicket = ticketRepository.save(ticket);
        return modelMapper.map(updatedTicket, TicketDTO.class);
    }

    @Override
    public TicketDTO updateTicketStatus(Long ticketId, Status newStatus){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()-> new ResourceNotFoundException("Ticket not found with ID: " + ticketId));
        ticket.setStatus(newStatus);
        ticket.setUpdatedAt(LocalDateTime.now());

        Ticket updatedTicket = ticketRepository.save(ticket);
        return modelMapper.map(updatedTicket, TicketDTO.class);
    }

    @Override
    public List<TicketDTO> getTicketsByStatus(Status status){
        List<Ticket> tickets = ticketRepository.findByStatus(status);
        return tickets.stream()
                .map(ticket->modelMapper.map(ticket, TicketDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<TicketDTO> getTicketsAssignedTo(Long agentId){
        User agent = userRepository.findById(agentId)
                .orElseThrow(()-> new ResourceNotFoundException("Agent not found with ID: " + agentId));
        List<Ticket> tickets = ticketRepository.findByAssignedTo(agent);
        return tickets.stream()
                .map(ticket->modelMapper.map(ticket, TicketDTO.class)).collect(Collectors.toList());
    }



}
