package com.baymak.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequestDto {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone cannot be empty")
    private String phone;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}
