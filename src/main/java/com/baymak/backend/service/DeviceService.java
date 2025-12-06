package com.baymak.backend.service;

import com.baymak.backend.dto.DeviceRequestDto;
import com.baymak.backend.dto.DeviceResponseDto;
import java.util.List;

public interface DeviceService {
    List<DeviceResponseDto> getMyDevices(String userEmail);
    DeviceResponseDto getDeviceById(Long id, String userEmail);
    DeviceResponseDto createDevice(DeviceRequestDto dto, String userEmail);
    DeviceResponseDto updateDevice(Long id, DeviceRequestDto dto, String userEmail);
    void deleteDevice(Long id, String userEmail);
}

