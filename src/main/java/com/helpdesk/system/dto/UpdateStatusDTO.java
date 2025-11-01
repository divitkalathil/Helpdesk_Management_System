package com.helpdesk.system.dto;

import com.helpdesk.system.model.Status;
import lombok.Data;

@Data
public class UpdateStatusDTO {
    private Status newStatus;
}
