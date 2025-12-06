package com.baymak.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequestDto {
    @NotNull(message = "Device ID cannot be null")
    private Long deviceId;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    @NotNull(message = "Time cannot be null")
    private LocalTime time;
}

