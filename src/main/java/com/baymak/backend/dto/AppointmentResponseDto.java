package com.baymak.backend.dto;

import com.baymak.backend.model.AppointmentStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
@Builder
public class AppointmentResponseDto {
    private Long id;
    private Long userId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;
    private Long deviceId;
    private String deviceType;
    private String deviceModel;
    private Long technicianId;
    private String technicianName;
    private LocalDate date;
    private LocalTime time;
    private AppointmentStatus status;
    private String problemDescription;
    private LocalDateTime createdAt;
}

