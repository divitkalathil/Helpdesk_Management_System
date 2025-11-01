package com.helpdesk.system.dto;

import com.helpdesk.system.model.Priority;
import lombok.Data;

@Data
public class TicketCreationDTO {
    private String title;
    private String description;
    private Priority priority;
    private Long createdById;

}
