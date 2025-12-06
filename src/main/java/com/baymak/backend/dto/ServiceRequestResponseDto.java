package com.baymak.backend.dto;

import com.baymak.backend.model.Status;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ServiceRequestResponseDto {
    private Long id;
    private String title;
    private String description;
    private String deviceType;
    private Status status;
    private LocalDateTime createdAt;
}

