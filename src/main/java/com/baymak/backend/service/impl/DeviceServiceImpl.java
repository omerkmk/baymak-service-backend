package com.baymak.backend.service.impl;

import com.baymak.backend.dto.DeviceRequestDto;
import com.baymak.backend.dto.DeviceResponseDto;
import com.baymak.backend.exception.NotFoundException;
import com.baymak.backend.model.Device;
import com.baymak.backend.model.User;
import com.baymak.backend.repository.DeviceRepository;
import com.baymak.backend.repository.UserRepository;
import com.baymak.backend.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DeviceResponseDto> getMyDevices(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));

        return deviceRepository.findByUser(user).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DeviceResponseDto getDeviceById(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));

        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found with id: " + id));

        if (!device.getUser().getId().equals(user.getId())) {
            throw new NotFoundException("Device not found with id: " + id);
        }

        return mapToDto(device);
    }

    @Override
    public DeviceResponseDto createDevice(DeviceRequestDto dto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));

        Device device = Device.builder()
                .user(user)
                .deviceType(dto.getDeviceType())
                .model(dto.getModel())
                .serialNumber(dto.getSerialNumber())
                .build();

        Device saved = deviceRepository.save(device);
        return mapToDto(saved);
    }

    @Override
    public DeviceResponseDto updateDevice(Long id, DeviceRequestDto dto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));

        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found with id: " + id));

        if (!device.getUser().getId().equals(user.getId())) {
            throw new NotFoundException("Device not found with id: " + id);
        }

        device.setDeviceType(dto.getDeviceType());
        device.setModel(dto.getModel());
        device.setSerialNumber(dto.getSerialNumber());

        Device updated = deviceRepository.save(device);
        return mapToDto(updated);
    }

    @Override
    public void deleteDevice(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));

        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found with id: " + id));

        if (!device.getUser().getId().equals(user.getId())) {
            throw new NotFoundException("Device not found with id: " + id);
        }

        deviceRepository.delete(device);
    }

    private DeviceResponseDto mapToDto(Device device) {
        return DeviceResponseDto.builder()
                .id(device.getId())
                .userId(device.getUser().getId())
                .deviceType(device.getDeviceType())
                .model(device.getModel())
                .serialNumber(device.getSerialNumber())
                .createdAt(device.getCreatedAt())
                .build();
    }
}

