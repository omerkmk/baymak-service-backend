package com.baymak.backend.service;

import com.baymak.backend.dto.TechnicianRequestDto;
import com.baymak.backend.dto.TechnicianResponseDto;
import java.util.List;
import java.util.Optional;

public interface TechnicianService {
    List<TechnicianResponseDto> getAllTechnicians();
    Optional<TechnicianResponseDto> getTechnicianById(Long id);
    TechnicianResponseDto createTechnician(TechnicianRequestDto dto);
    TechnicianResponseDto updateTechnician(Long id, TechnicianRequestDto dto);
    void deleteTechnician(Long id);
}

