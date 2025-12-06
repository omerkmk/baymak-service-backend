package com.baymak.backend.controller;

import com.baymak.backend.dto.ServiceReportRequestDto;
import com.baymak.backend.dto.ServiceReportResponseDto;
import com.baymak.backend.service.ServiceReportService;
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
@RequestMapping("/api/service-reports")
@RequiredArgsConstructor
@Tag(name = "Service Reports", description = "Service report management operations")
public class ServiceReportController {

    private final ServiceReportService serviceReportService;

    @PostMapping
    @Operation(summary = "Servis raporu oluştur", description = "Teknisyen tarafından tamamlanan randevu için servis raporu oluşturulur. Randevu otomatik olarak COMPLETED durumuna geçer.")
    public ResponseEntity<ServiceReportResponseDto> createServiceReport(
            @Valid @RequestBody ServiceReportRequestDto dto,
            Authentication authentication) {
        String technicianEmail = authentication.getName();
        ServiceReportResponseDto created = serviceReportService.createServiceReport(dto, technicianEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create("/api/service-reports/" + created.getId()))
                .body(created);
    }

    @GetMapping("/my")
    @Operation(summary = "Kendi servis raporlarımı listele", description = "Giriş yapmış teknisyenin oluşturduğu tüm servis raporlarını listeler.")
    public ResponseEntity<List<ServiceReportResponseDto>> getMyServiceReports(Authentication authentication) {
        String technicianEmail = authentication.getName();
        List<ServiceReportResponseDto> reports = serviceReportService.getMyServiceReports(technicianEmail);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/appointment/{appointmentId}")
    @Operation(summary = "Randevuya ait servis raporu", description = "Belirli bir randevuya ait servis raporunu getirir.")
    public ResponseEntity<ServiceReportResponseDto> getServiceReportByAppointmentId(@PathVariable Long appointmentId) {
        ServiceReportResponseDto report = serviceReportService.getServiceReportByAppointmentId(appointmentId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Servis raporu detayı", description = "Belirli bir servis raporunun detaylarını getirir.")
    public ResponseEntity<ServiceReportResponseDto> getServiceReportById(@PathVariable Long id) {
        ServiceReportResponseDto report = serviceReportService.getServiceReportById(id);
        return ResponseEntity.ok(report);
    }
}

