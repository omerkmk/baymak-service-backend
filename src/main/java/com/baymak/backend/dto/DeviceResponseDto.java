package com.baymak.backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class DeviceResponseDto {
    private Long id;
    private Long userId;
    private String deviceType;
    private String model;
    private String serialNumber;
    private LocalDateTime createdAt;
}

