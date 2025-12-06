package com.baymak.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TechnicianRequestDto {
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Phone cannot be empty")
    private String phone;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    private String specialization;
}

