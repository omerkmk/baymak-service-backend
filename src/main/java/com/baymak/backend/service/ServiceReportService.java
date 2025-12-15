package com.baymak.backend.service;

import com.baymak.backend.dto.ServiceReportRequestDto;
import com.baymak.backend.dto.ServiceReportResponseDto;
import java.util.List;

public interface ServiceReportService {
    ServiceReportResponseDto createServiceReport(ServiceReportRequestDto dto, String technicianEmail);
    List<ServiceReportResponseDto> getMyServiceReports(String technicianEmail);
    List<ServiceReportResponseDto> getAllServiceReports();
    ServiceReportResponseDto getServiceReportByAppointmentId(Long appointmentId);
    ServiceReportResponseDto getServiceReportById(Long id);
}

