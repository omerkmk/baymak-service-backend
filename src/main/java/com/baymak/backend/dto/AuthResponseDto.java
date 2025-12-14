package com.baymak.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
    private String email;
    private String role;
    private String message;
    // User information for profile page
    private Long id;
    private String name;
    private String phone;
    private String address;
}

