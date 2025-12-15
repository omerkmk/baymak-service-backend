package com.baymak.backend.service.impl;

import com.baymak.backend.dto.AppointmentAssignDto;
import com.baymak.backend.dto.AppointmentRequestDto;
import com.baymak.backend.dto.AppointmentResponseDto;
import com.baymak.backend.dto.AppointmentStatusUpdateDto;
import com.baymak.backend.exception.BadRequestException;
import com.baymak.backend.exception.NotFoundException;
import com.baymak.backend.model.*;
import com.baymak.backend.repository.AppointmentRepository;
import com.baymak.backend.repository.DeviceRepository;
import com.baymak.backend.repository.TechnicianRepository;
import com.baymak.backend.repository.UserRepository;
import com.baymak.backend.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final TechnicianRepository technicianRepository;

    @Override
    public AppointmentResponseDto createAppointment(AppointmentRequestDto dto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));

        Device device = deviceRepository.findById(dto.getDeviceId())
                .orElseThrow(() -> new NotFoundException("Device not found with id: " + dto.getDeviceId()));

        if (!device.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Device does not belong to the user");
        }

        Appointment appointment = Appointment.builder()
                .user(user)
                .device(device)
                .date(dto.getDate())
                .time(dto.getTime())
                .problemDescription(dto.getProblemDescription())
                .status(AppointmentStatus.PENDING)
                .build();

        Appointment saved = appointmentRepository.save(appointment);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDto> getMyAppointments(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));

        return appointmentRepository.findByUser(user).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponseDto getAppointmentById(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));

        Appointment appointment = appointmentRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new NotFoundException("Appointment not found with id: " + id));

        return mapToDto(appointment);
    }

    @Override
    public void cancelAppointment(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));

        Appointment appointment = appointmentRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new NotFoundException("Appointment not found with id: " + id));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot cancel a completed appointment");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDto> getAssignedAppointments(String technicianEmail) {
        Technician technician = technicianRepository.findByEmail(technicianEmail)
                .orElseThrow(() -> new NotFoundException("Technician not found with email: " + technicianEmail));

        return appointmentRepository.findByTechnician(technician).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponseDto updateAppointmentStatus(Long id, AppointmentStatusUpdateDto dto, String technicianEmail) {
        Technician technician = technicianRepository.findByEmail(technicianEmail)
                .orElseThrow(() -> new NotFoundException("Technician not found with email: " + technicianEmail));

        Appointment appointment = appointmentRepository.findByIdAndTechnician(id, technician)
                .orElseThrow(() -> new NotFoundException("Appointment not found with id: " + id));

        appointment.setStatus(dto.getStatus());
        Appointment updated = appointmentRepository.save(appointment);
        return mapToDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponseDto assignTechnician(Long id, AppointmentAssignDto dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found with id: " + id));

        Technician technician = technicianRepository.findById(dto.getTechnicianId())
                .orElseThrow(() -> new NotFoundException("Technician not found with id: " + dto.getTechnicianId()));

        appointment.setTechnician(technician);
        appointment.setStatus(AppointmentStatus.ASSIGNED);
        Appointment updated = appointmentRepository.save(appointment);
        return mapToDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDto> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private AppointmentResponseDto mapToDto(Appointment appointment) {
        return AppointmentResponseDto.builder()
                .id(appointment.getId())
                .userId(appointment.getUser().getId())
                .deviceId(appointment.getDevice().getId())
                .deviceType(appointment.getDevice().getDeviceType())
                .deviceModel(appointment.getDevice().getModel())
                .technicianId(appointment.getTechnician() != null ? appointment.getTechnician().getId() : null)
                .technicianName(appointment.getTechnician() != null ? appointment.getTechnician().getName() : null)
                .date(appointment.getDate())
                .time(appointment.getTime())
                .status(appointment.getStatus())
                .problemDescription(appointment.getProblemDescription())
                .createdAt(appointment.getCreatedAt())
                .build();
    }
}

