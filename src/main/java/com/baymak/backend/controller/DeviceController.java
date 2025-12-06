package com.baymak.backend.controller;

import com.baymak.backend.dto.DeviceRequestDto;
import com.baymak.backend.dto.DeviceResponseDto;
import com.baymak.backend.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@Tag(name = "Devices", description = "Device management operations")
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping("/my")
    @Operation(summary = "Kendi cihazlarımı listele", description = "Giriş yapmış kullanıcının tüm cihazlarını listeler.")
    public ResponseEntity<List<DeviceResponseDto>> getMyDevices(Authentication authentication) {
        String userEmail = authentication.getName();
        List<DeviceResponseDto> devices = deviceService.getMyDevices(userEmail);
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Cihaz detayı", description = "Belirli bir cihazın detaylarını getirir. Kullanıcı sadece kendi cihazlarına erişebilir.")
    public ResponseEntity<DeviceResponseDto> getDeviceById(
            @PathVariable Long id,
            Authentication authentication) {
        String userEmail = authentication.getName();
        DeviceResponseDto device = deviceService.getDeviceById(id, userEmail);
        return ResponseEntity.ok(device);
    }

    @PostMapping
    @Operation(summary = "Yeni cihaz ekle", description = "Yeni bir cihaz ekler. Cihaz otomatik olarak giriş yapmış kullanıcıya atanır.")
    public ResponseEntity<DeviceResponseDto> createDevice(
            @Valid @RequestBody DeviceRequestDto dto,
            Authentication authentication) {
        String userEmail = authentication.getName();
        DeviceResponseDto created = deviceService.createDevice(dto, userEmail);
        return ResponseEntity.created(URI.create("/api/devices/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cihaz güncelle", description = "Mevcut bir cihazın bilgilerini günceller. Kullanıcı sadece kendi cihazlarını güncelleyebilir.")
    public ResponseEntity<DeviceResponseDto> updateDevice(
            @PathVariable Long id,
            @Valid @RequestBody DeviceRequestDto dto,
            Authentication authentication) {
        String userEmail = authentication.getName();
        DeviceResponseDto updated = deviceService.updateDevice(id, dto, userEmail);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cihaz sil", description = "Belirli bir cihazı siler. Kullanıcı sadece kendi cihazlarını silebilir.")
    public ResponseEntity<Void> deleteDevice(
            @PathVariable Long id,
            Authentication authentication) {
        String userEmail = authentication.getName();
        deviceService.deleteDevice(id, userEmail);
        return ResponseEntity.noContent().build();
    }
}

