package com.baymak.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceRequestDto {
    @NotBlank(message = "Device type cannot be empty")
    private String deviceType;

    private String model;

    private String serialNumber;
}

