package com.baymak.backend.service.impl;

import com.baymak.backend.dto.ServiceRequestRequestDto;
import com.baymak.backend.dto.ServiceRequestResponseDto;
import com.baymak.backend.exception.NotFoundException;
import com.baymak.backend.model.ServiceRequest;
import com.baymak.backend.model.Status;
import com.baymak.backend.model.User;
import com.baymak.backend.repository.ServiceRequestRepository;
import com.baymak.backend.repository.UserRepository;
import com.baymak.backend.service.ServiceRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceRequestServiceImpl implements ServiceRequestService {

    private final ServiceRequestRepository serviceRequestRepository;
    private final UserRepository userRepository;

    @Override
    public ServiceRequestResponseDto create(ServiceRequestRequestDto dto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));

        ServiceRequest serviceRequest = ServiceRequest.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .deviceType(dto.getDeviceType())
                .status(Status.OPEN)
                .user(user)
                .build();

        ServiceRequest saved = serviceRequestRepository.save(serviceRequest);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceRequestResponseDto> getMyRequests(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));

        return serviceRequestRepository.findByUser(user).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceRequestResponseDto getById(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));

        ServiceRequest serviceRequest = serviceRequestRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new NotFoundException("Service request not found with id: " + id));

        return mapToDto(serviceRequest);
    }

    @Override
    public ServiceRequestResponseDto updateStatus(Long id, Status newStatus) {
        if (newStatus == null) {
            throw new com.baymak.backend.exception.BadRequestException("Status cannot be null");
        }

        ServiceRequest serviceRequest = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service request not found with id: " + id));

        serviceRequest.setStatus(newStatus);
        ServiceRequest updated = serviceRequestRepository.save(serviceRequest);
        return mapToDto(updated);
    }

    @Override
    public void delete(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));

        ServiceRequest serviceRequest = serviceRequestRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new NotFoundException("Service request not found with id: " + id));

        serviceRequestRepository.delete(serviceRequest);
    }

    private ServiceRequestResponseDto mapToDto(ServiceRequest serviceRequest) {
        return ServiceRequestResponseDto.builder()
                .id(serviceRequest.getId())
                .title(serviceRequest.getTitle())
                .description(serviceRequest.getDescription())
                .deviceType(serviceRequest.getDeviceType())
                .status(serviceRequest.getStatus())
                .createdAt(serviceRequest.getCreatedAt())
                .build();
    }
}

