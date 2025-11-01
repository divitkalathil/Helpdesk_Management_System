package com.helpdesk.system.dto;

import com.helpdesk.system.model.Priority;
import com.helpdesk.system.model.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketDTO {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String createdByUsername;
    private String assignedToUsername;

}
