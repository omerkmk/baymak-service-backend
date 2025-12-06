package com.baymak.backend.service;

import com.baymak.backend.dto.ServiceRequestRequestDto;
import com.baymak.backend.dto.ServiceRequestResponseDto;
import com.baymak.backend.model.Status;

import java.util.List;

public interface ServiceRequestService {
    ServiceRequestResponseDto create(ServiceRequestRequestDto dto, String userEmail);
    List<ServiceRequestResponseDto> getMyRequests(String userEmail);
    ServiceRequestResponseDto getById(Long id, String userEmail);
    ServiceRequestResponseDto updateStatus(Long id, Status newStatus);
    void delete(Long id, String userEmail);
}

