package com.baymak.backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class TechnicianResponseDto {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String specialization;
    private LocalDateTime createdAt;
}

