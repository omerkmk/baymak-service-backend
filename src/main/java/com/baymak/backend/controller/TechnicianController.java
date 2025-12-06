package com.baymak.backend.controller;

import com.baymak.backend.dto.TechnicianRequestDto;
import com.baymak.backend.dto.TechnicianResponseDto;
import com.baymak.backend.service.TechnicianService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/technicians")
@RequiredArgsConstructor
@Tag(name = "Technicians", description = "Technician management operations")
public class TechnicianController {

    private final TechnicianService technicianService;

    @GetMapping
    @Operation(summary = "Tüm teknisyenleri listele", description = "Sistemdeki tüm teknisyenleri listeler. JWT token gereklidir.")
    public ResponseEntity<List<TechnicianResponseDto>> getAllTechnicians() {
        return ResponseEntity.ok(technicianService.getAllTechnicians());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Teknisyen detayı", description = "Belirli bir teknisyenin detaylarını getirir. JWT token gereklidir.")
    public ResponseEntity<TechnicianResponseDto> getTechnicianById(@PathVariable Long id) {
        return technicianService.getTechnicianById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Yeni teknisyen oluştur", description = "Yeni bir teknisyen oluşturur. Email unique olmalıdır. JWT token gereklidir.")
    public ResponseEntity<TechnicianResponseDto> createTechnician(@Valid @RequestBody TechnicianRequestDto dto) {
        TechnicianResponseDto created = technicianService.createTechnician(dto);
        return ResponseEntity.created(URI.create("/api/technicians/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Teknisyen güncelle", description = "Mevcut bir teknisyenin bilgilerini günceller. JWT token gereklidir.")
    public ResponseEntity<TechnicianResponseDto> updateTechnician(
            @PathVariable Long id,
            @Valid @RequestBody TechnicianRequestDto dto) {
        TechnicianResponseDto updated = technicianService.updateTechnician(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Teknisyen sil", description = "Belirli bir teknisyeni siler. JWT token gereklidir.")
    public ResponseEntity<Void> deleteTechnician(@PathVariable Long id) {
        technicianService.deleteTechnician(id);
        return ResponseEntity.noContent().build();
    }
}

