package com.baymak.backend.controller;

import com.baymak.backend.dto.ServiceRequestRequestDto;
import com.baymak.backend.dto.ServiceRequestResponseDto;
import com.baymak.backend.model.Status;
import com.baymak.backend.service.ServiceRequestService;
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
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@Tag(name = "Service Requests", description = "Customer service request operations")
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    @PostMapping
    @Operation(summary = "Yeni servis talebi oluştur", description = "Geçerli bir kullanıcı tarafından yeni bir servis talebi oluşturulur. Talep otomatik olarak OPEN durumunda başlar.")
    public ResponseEntity<ServiceRequestResponseDto> create(
            @Valid @RequestBody ServiceRequestRequestDto dto,
            Authentication authentication) {
        String userEmail = authentication.getName();
        ServiceRequestResponseDto created = serviceRequestService.create(dto, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create("/api/requests/" + created.getId()))
                .body(created);
    }

    @GetMapping("/my")
    @Operation(summary = "Kendi servis taleplerimi listele", description = "Giriş yapmış kullanıcının tüm servis taleplerini listeler.")
    public ResponseEntity<List<ServiceRequestResponseDto>> getMyRequests(Authentication authentication) {
        String userEmail = authentication.getName();
        List<ServiceRequestResponseDto> requests = serviceRequestService.getMyRequests(userEmail);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Servis talebi detayı", description = "Belirli bir servis talebinin detaylarını getirir. Kullanıcı sadece kendi taleplerine erişebilir.")
    public ResponseEntity<ServiceRequestResponseDto> getById(
            @PathVariable Long id,
            Authentication authentication) {
        String userEmail = authentication.getName();
        ServiceRequestResponseDto request = serviceRequestService.getById(id, userEmail);
        return ResponseEntity.ok(request);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Servis talebi durumunu güncelle", description = "Servis talebinin durumunu günceller. Geçerli durumlar: OPEN, IN_PROGRESS, COMPLETED")
    public ResponseEntity<ServiceRequestResponseDto> updateStatus(
            @PathVariable Long id,
            @RequestBody Status newStatus) {
        ServiceRequestResponseDto updated = serviceRequestService.updateStatus(id, newStatus);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Servis talebini sil", description = "Belirli bir servis talebini siler. Kullanıcı sadece kendi taleplerini silebilir.")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication) {
        String userEmail = authentication.getName();
        serviceRequestService.delete(id, userEmail);
        return ResponseEntity.noContent().build();
    }
}

