package com.baymak.backend.service.impl;

import com.baymak.backend.dto.ServiceReportRequestDto;
import com.baymak.backend.dto.ServiceReportResponseDto;
import com.baymak.backend.exception.AlreadyExistsException;
import com.baymak.backend.exception.BadRequestException;
import com.baymak.backend.exception.NotFoundException;
import com.baymak.backend.model.*;
import com.baymak.backend.repository.AppointmentRepository;
import com.baymak.backend.repository.ServiceReportRepository;
import com.baymak.backend.repository.TechnicianRepository;
import com.baymak.backend.service.ServiceReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceReportServiceImpl implements ServiceReportService {

    private final ServiceReportRepository serviceReportRepository;
    private final AppointmentRepository appointmentRepository;
    private final TechnicianRepository technicianRepository;

    @Override
    public ServiceReportResponseDto createServiceReport(ServiceReportRequestDto dto, String technicianEmail) {
        Technician technician = technicianRepository.findByEmail(technicianEmail)
                .orElseThrow(() -> new NotFoundException("Technician not found with email: " + technicianEmail));

        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new NotFoundException("Appointment not found with id: " + dto.getAppointmentId()));

        if (appointment.getTechnician() == null || !appointment.getTechnician().getId().equals(technician.getId())) {
            throw new BadRequestException("Appointment is not assigned to this technician");
        }

        if (appointment.getStatus() != AppointmentStatus.IN_PROGRESS && appointment.getStatus() != AppointmentStatus.ASSIGNED) {
            throw new BadRequestException("Cannot create service report for appointment with status: " + appointment.getStatus());
        }

        // Check if service report already exists
        if (serviceReportRepository.findByAppointment(appointment).isPresent()) {
            throw new AlreadyExistsException("Service report already exists for this appointment");
        }

        ServiceReport serviceReport = ServiceReport.builder()
                .appointment(appointment)
                .technician(technician)
                .description(dto.getDescription())
                .partsUsed(dto.getPartsUsed())
                .price(dto.getPrice())
                .build();

        // Update appointment status to COMPLETED
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        ServiceReport saved = serviceReportRepository.save(serviceReport);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceReportResponseDto> getMyServiceReports(String technicianEmail) {
        Technician technician = technicianRepository.findByEmail(technicianEmail)
                .orElseThrow(() -> new NotFoundException("Technician not found with email: " + technicianEmail));

        return serviceReportRepository.findByTechnician(technician).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceReportResponseDto getServiceReportByAppointmentId(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment not found with id: " + appointmentId));

        ServiceReport serviceReport = serviceReportRepository.findByAppointment(appointment)
                .orElseThrow(() -> new NotFoundException("Service report not found for appointment id: " + appointmentId));

        return mapToDto(serviceReport);
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceReportResponseDto getServiceReportById(Long id) {
        ServiceReport serviceReport = serviceReportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service report not found with id: " + id));

        return mapToDto(serviceReport);
    }

    private ServiceReportResponseDto mapToDto(ServiceReport serviceReport) {
        return ServiceReportResponseDto.builder()
                .id(serviceReport.getId())
                .appointmentId(serviceReport.getAppointment().getId())
                .technicianId(serviceReport.getTechnician().getId())
                .technicianName(serviceReport.getTechnician().getName())
                .description(serviceReport.getDescription())
                .partsUsed(serviceReport.getPartsUsed())
                .price(serviceReport.getPrice())
                .createdAt(serviceReport.getCreatedAt())
                .build();
    }
}

