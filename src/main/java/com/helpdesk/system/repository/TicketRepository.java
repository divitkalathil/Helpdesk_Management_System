package com.helpdesk.system.repository;

import com.helpdesk.system.model.Status;
import com.helpdesk.system.model.Ticket;
import com.helpdesk.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatus(Status status);
    List<Ticket> findByAssignedTo(User agent);
}
