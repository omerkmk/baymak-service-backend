package com.baymak.backend.controller;

import com.baymak.backend.dto.AuthRequestDto;
import com.baymak.backend.dto.AuthResponseDto;
import com.baymak.backend.dto.PasswordResetRequestDto;
import com.baymak.backend.dto.TechnicianRequestDto;
import com.baymak.backend.dto.TechnicianResponseDto;
import com.baymak.backend.dto.UserRequestDto;
import com.baymak.backend.dto.UserResponseDto;
import com.baymak.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Register & Login APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Müşteri kaydı", description = "Yeni bir müşteri kaydı oluşturur. Email unique olmalıdır. Role: CUSTOMER")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto userDto) {
        UserResponseDto created = authService.register(userDto);
        return ResponseEntity.created(URI.create("/api/users/" + created.getId())).body(created);
    }

    @PostMapping("/register/technician")
    @Operation(summary = "Teknisyen kaydı", description = "Yeni bir teknisyen kaydı oluşturur. Email unique olmalıdır. Role: TECHNICIAN")
    public ResponseEntity<TechnicianResponseDto> registerTechnician(@Valid @RequestBody TechnicianRequestDto technicianDto) {
        TechnicianResponseDto created = authService.registerTechnician(technicianDto);
        return ResponseEntity.created(URI.create("/api/technicians/" + created.getId())).body(created);
    }

    @PostMapping("/login")
    @Operation(summary = "Kullanıcı girişi", description = "Email ve şifre ile giriş yapar. Müşteri veya teknisyen olabilir. Başarılı girişte JWT token döner.")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authDto) {
        AuthResponseDto response = authService.login(authDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Şifre sıfırlama", description = "Email ve yeni şifre ile şifre sıfırlama işlemi yapar.")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetRequestDto dto) {
        authService.resetPassword(dto);
        return ResponseEntity.ok().body(java.util.Map.of("message", "Password reset successfully"));
    }
}

