package com.baymak.backend.service.impl;

import com.baymak.backend.dto.TechnicianRequestDto;
import com.baymak.backend.dto.TechnicianResponseDto;
import com.baymak.backend.exception.AlreadyExistsException;
import com.baymak.backend.exception.NotFoundException;
import com.baymak.backend.model.Technician;
import com.baymak.backend.model.User;
import com.baymak.backend.repository.TechnicianRepository;
import com.baymak.backend.repository.UserRepository;
import com.baymak.backend.service.TechnicianService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TechnicianServiceImpl implements TechnicianService {

    private final TechnicianRepository technicianRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<TechnicianResponseDto> getAllTechnicians() {
        return technicianRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TechnicianResponseDto> getTechnicianById(Long id) {
        return technicianRepository.findById(id)
                .map(this::mapToDto);
    }

    @Override
    public TechnicianResponseDto createTechnician(TechnicianRequestDto dto) {
        // Email kontrolü (hem User hem Technician tablosunda)
        if (userRepository.existsByEmail(dto.getEmail()) || technicianRepository.existsByEmail(dto.getEmail())) {
            throw new AlreadyExistsException("Technician with email " + dto.getEmail() + " already exists");
        }

        // Password hashleme
        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        // User entity oluştur (role=TECHNICIAN) - Login için gerekli
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address("") // Technician için address opsiyonel
                .password(hashedPassword)
                .role(User.Role.TECHNICIAN)
                .build();

        User savedUser = userRepository.save(user);

        // Technician entity oluştur
        Technician technician = Technician.builder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .password(hashedPassword)
                .specialization(dto.getSpecialization())
                .build();

        Technician saved = technicianRepository.save(technician);
        return mapToDto(saved);
    }

    @Override
    public TechnicianResponseDto updateTechnician(Long id, TechnicianRequestDto dto) {
        Technician technician = technicianRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Technician not found with id: " + id));

        technician.setName(dto.getName());
        technician.setPhone(dto.getPhone());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            technician.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        technician.setSpecialization(dto.getSpecialization());

        Technician updated = technicianRepository.save(technician);
        return mapToDto(updated);
    }

    @Override
    public void deleteTechnician(Long id) {
        if (!technicianRepository.existsById(id)) {
            throw new NotFoundException("Technician not found with id: " + id);
        }
        technicianRepository.deleteById(id);
    }

    private TechnicianResponseDto mapToDto(Technician technician) {
        return TechnicianResponseDto.builder()
                .id(technician.getId())
                .name(technician.getName())
                .phone(technician.getPhone())
                .email(technician.getEmail())
                .specialization(technician.getSpecialization())
                .createdAt(technician.getCreatedAt())
                .build();
    }
}

