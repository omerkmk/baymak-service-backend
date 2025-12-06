package com.baymak.backend.service;

import com.baymak.backend.dto.AppointmentAssignDto;
import com.baymak.backend.dto.AppointmentRequestDto;
import com.baymak.backend.dto.AppointmentResponseDto;
import com.baymak.backend.dto.AppointmentStatusUpdateDto;
import com.baymak.backend.model.AppointmentStatus;
import java.util.List;

public interface AppointmentService {
    // Customer operations
    AppointmentResponseDto createAppointment(AppointmentRequestDto dto, String userEmail);
    List<AppointmentResponseDto> getMyAppointments(String userEmail);
    AppointmentResponseDto getAppointmentById(Long id, String userEmail);
    void cancelAppointment(Long id, String userEmail);
    
    // Technician operations
    List<AppointmentResponseDto> getAssignedAppointments(String technicianEmail);
    AppointmentResponseDto updateAppointmentStatus(Long id, AppointmentStatusUpdateDto dto, String technicianEmail);
    
    // Admin operations
    List<AppointmentResponseDto> getAllAppointments();
    AppointmentResponseDto assignTechnician(Long id, AppointmentAssignDto dto);
    List<AppointmentResponseDto> getAppointmentsByStatus(AppointmentStatus status);
}

