package com.baymak.backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ServiceReportResponseDto {
    private Long id;
    private Long appointmentId;
    private Long technicianId;
    private String technicianName;
    private String description;
    private String partsUsed;
    private Double price;
    private LocalDateTime createdAt;
}

