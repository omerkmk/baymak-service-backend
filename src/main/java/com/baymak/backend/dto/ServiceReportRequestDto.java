package com.baymak.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ServiceReportRequestDto {
    @NotNull(message = "Appointment ID cannot be null")
    private Long appointmentId;

    private String description;

    private String partsUsed;

    private Double price;
}

