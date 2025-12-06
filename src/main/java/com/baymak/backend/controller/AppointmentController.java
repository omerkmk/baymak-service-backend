package com.baymak.backend.controller;

import com.baymak.backend.dto.AppointmentAssignDto;
import com.baymak.backend.dto.AppointmentRequestDto;
import com.baymak.backend.dto.AppointmentResponseDto;
import com.baymak.backend.dto.AppointmentStatusUpdateDto;
import com.baymak.backend.model.AppointmentStatus;
import com.baymak.backend.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Appointment management operations")
public class AppointmentController {

    private final AppointmentService appointmentService;

    // Customer endpoints
    @PostMapping
    @Operation(summary = "Yeni randevu oluştur", description = "Giriş yapmış müşteri tarafından yeni bir randevu oluşturulur. Randevu otomatik olarak PENDING durumunda başlar.")
    public ResponseEntity<AppointmentResponseDto> createAppointment(
            @Valid @RequestBody AppointmentRequestDto dto,
            Authentication authentication) {
        String userEmail = authentication.getName();
        AppointmentResponseDto created = appointmentService.createAppointment(dto, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create("/api/appointments/" + created.getId()))
                .body(created);
    }

    @GetMapping("/my")
    @Operation(summary = "Kendi randevularımı listele", description = "Giriş yapmış müşterinin tüm randevularını listeler.")
    public ResponseEntity<List<AppointmentResponseDto>> getMyAppointments(Authentication authentication) {
        String userEmail = authentication.getName();
        List<AppointmentResponseDto> appointments = appointmentService.getMyAppointments(userEmail);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/my/{id}")
    @Operation(summary = "Randevu detayı", description = "Belirli bir randevunun detaylarını getirir. Müşteri sadece kendi randevularına erişebilir.")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(
            @PathVariable Long id,
            Authentication authentication) {
        String userEmail = authentication.getName();
        AppointmentResponseDto appointment = appointmentService.getAppointmentById(id, userEmail);
        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/my/{id}/cancel")
    @Operation(summary = "Randevu iptal et", description = "Belirli bir randevuyu iptal eder. Müşteri sadece kendi randevularını iptal edebilir.")
    public ResponseEntity<Void> cancelAppointment(
            @PathVariable Long id,
            Authentication authentication) {
        String userEmail = authentication.getName();
        appointmentService.cancelAppointment(id, userEmail);
        return ResponseEntity.noContent().build();
    }

    // Technician endpoints
    @GetMapping("/assigned")
    @Operation(summary = "Atanmış randevuları listele", description = "Giriş yapmış teknisyenin kendisine atanmış randevularını listeler.")
    public ResponseEntity<List<AppointmentResponseDto>> getAssignedAppointments(Authentication authentication) {
        String technicianEmail = authentication.getName();
        List<AppointmentResponseDto> appointments = appointmentService.getAssignedAppointments(technicianEmail);
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Randevu durumunu güncelle", description = "Randevunun durumunu günceller. Teknisyen sadece kendisine atanmış randevuların durumunu güncelleyebilir.")
    public ResponseEntity<AppointmentResponseDto> updateAppointmentStatus(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentStatusUpdateDto dto,
            Authentication authentication) {
        String technicianEmail = authentication.getName();
        AppointmentResponseDto updated = appointmentService.updateAppointmentStatus(id, dto, technicianEmail);
        return ResponseEntity.ok(updated);
    }

    // Admin endpoints
    @GetMapping("/all")
    @Operation(summary = "Tüm randevuları listele", description = "Sistemdeki tüm randevuları listeler. Admin yetkisi gereklidir.")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments() {
        List<AppointmentResponseDto> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/{id}/assign")
    @Operation(summary = "Teknisyen ata", description = "Randevuya teknisyen atar. Admin yetkisi gereklidir.")
    public ResponseEntity<AppointmentResponseDto> assignTechnician(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentAssignDto dto) {
        AppointmentResponseDto updated = appointmentService.assignTechnician(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Duruma göre randevuları listele", description = "Belirli bir durumdaki randevuları listeler. Admin yetkisi gereklidir.")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByStatus(@PathVariable AppointmentStatus status) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByStatus(status);
        return ResponseEntity.ok(appointments);
    }
}

