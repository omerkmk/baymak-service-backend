package com.baymak.backend.dto;

import com.baymak.backend.model.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppointmentStatusUpdateDto {
    @NotNull(message = "Status cannot be null")
    private AppointmentStatus status;
}

