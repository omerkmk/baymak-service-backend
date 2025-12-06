package com.baymak.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppointmentAssignDto {
    @NotNull(message = "Technician ID cannot be null")
    private Long technicianId;
}

