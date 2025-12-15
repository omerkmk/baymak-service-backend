package com.baymak.backend.service.impl;

import com.baymak.backend.dto.AuthRequestDto;
import com.baymak.backend.dto.AuthResponseDto;
import com.baymak.backend.dto.PasswordResetRequestDto;
import com.baymak.backend.dto.TechnicianRequestDto;
import com.baymak.backend.dto.TechnicianResponseDto;
import com.baymak.backend.dto.UserRequestDto;
import com.baymak.backend.dto.UserResponseDto;
import com.baymak.backend.exception.AlreadyExistsException;
import com.baymak.backend.exception.BadRequestException;
import com.baymak.backend.exception.NotFoundException;
import com.baymak.backend.model.Technician;
import com.baymak.backend.model.User;
import com.baymak.backend.repository.TechnicianRepository;
import com.baymak.backend.repository.UserRepository;
import com.baymak.backend.security.JwtTokenProvider;
import com.baymak.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final TechnicianRepository technicianRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserResponseDto register(UserRequestDto dto) {
        // Email kontrolü
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new AlreadyExistsException("User with email " + dto.getEmail() + " already exists");
        }

        // Password hashleme
        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        // User entity oluşturma
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .password(hashedPassword)
                .role(User.Role.CUSTOMER)
                .build();

        // Kaydetme
        User savedUser = userRepository.save(user);

        // DTO'ya dönüştürme
        return mapToDto(savedUser);
    }

    @Override
    public TechnicianResponseDto registerTechnician(TechnicianRequestDto dto) {
        // Email kontrolü (hem User hem Technician tablosunda)
        if (userRepository.existsByEmail(dto.getEmail()) || technicianRepository.existsByEmail(dto.getEmail())) {
            throw new AlreadyExistsException("Technician with email " + dto.getEmail() + " already exists");
        }

        // Password hashleme
        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        // User entity oluştur (role=TECHNICIAN)
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

        Technician savedTechnician = technicianRepository.save(technician);

        // TechnicianResponseDto döndür
        return TechnicianResponseDto.builder()
                .id(savedTechnician.getId())
                .name(savedTechnician.getName())
                .phone(savedTechnician.getPhone())
                .email(savedTechnician.getEmail())
                .specialization(savedTechnician.getSpecialization())
                .createdAt(savedTechnician.getCreatedAt())
                .build();
    }

    @Override
    public AuthResponseDto login(AuthRequestDto dto) {
        // Email ile kullanıcıyı bul (User tablosundan - hem CUSTOMER hem TECHNICIAN burada)
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NotFoundException("User with email " + dto.getEmail() + " not found"));

        // Password kontrolü
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid password");
        }

        // JWT token üret
        String token = jwtTokenProvider.generateToken(user);

        // Başarılı login response
        return AuthResponseDto.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .message("Login successful")
                .id(user.getId())
                .name(user.getName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .build();
    }

    @Override
    public void resetPassword(PasswordResetRequestDto dto) {
        // Email ile kullanıcıyı bul
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NotFoundException("User with email " + dto.getEmail() + " not found"));

        // Yeni şifreyi hashle
        String hashedPassword = passwordEncoder.encode(dto.getNewPassword());

        // Şifreyi güncelle
        user.setPassword(hashedPassword);
        userRepository.save(user);

        // Eğer Technician ise, Technician tablosundaki şifreyi de güncelle
        if (user.getRole() == User.Role.TECHNICIAN) {
            technicianRepository.findByEmail(dto.getEmail()).ifPresent(technician -> {
                technician.setPassword(hashedPassword);
                technicianRepository.save(technician);
            });
        }
    }

    private UserResponseDto mapToDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

