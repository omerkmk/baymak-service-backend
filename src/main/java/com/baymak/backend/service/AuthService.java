package com.baymak.backend.service;

import com.baymak.backend.dto.AuthRequestDto;
import com.baymak.backend.dto.AuthResponseDto;
import com.baymak.backend.dto.PasswordResetRequestDto;
import com.baymak.backend.dto.TechnicianRequestDto;
import com.baymak.backend.dto.TechnicianResponseDto;
import com.baymak.backend.dto.UserRequestDto;
import com.baymak.backend.dto.UserResponseDto;

public interface AuthService {
    UserResponseDto register(UserRequestDto dto);
    TechnicianResponseDto registerTechnician(TechnicianRequestDto dto);
    AuthResponseDto login(AuthRequestDto dto);
    void resetPassword(PasswordResetRequestDto dto);
}

