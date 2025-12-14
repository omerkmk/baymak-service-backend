package com.baymak.backend.service.impl;

import com.baymak.backend.dto.UserRequestDto;
import com.baymak.backend.dto.UserResponseDto;
import com.baymak.backend.model.User;
import com.baymak.backend.repository.UserRepository;
import com.baymak.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    private User mapToEntity(UserRequestDto dto) {
        // Password is required for user creation
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required for user creation");
        }
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .password(hashedPassword)
                .role(User.Role.CUSTOMER) // Default role
                .build();
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponseDto> getUserById(Long id) {
        return userRepository.findById(id).map(this::mapToDto);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userDto) {
        User user = mapToEntity(userDto);
        User saved = userRepository.save(user);
        return mapToDto(saved);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userDto) {
        return userRepository.findById(id)
                .map(existing -> {
                    existing.setName(userDto.getName());
                    existing.setEmail(userDto.getEmail());
                    existing.setPhone(userDto.getPhone());
                    existing.setAddress(userDto.getAddress());
                    // Password güncellenirse hashlenir
                    if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                        existing.setPassword(passwordEncoder.encode(userDto.getPassword()));
                    }
                    // Role güncellenmez, mevcut role korunur
                    return mapToDto(userRepository.save(existing));
                })
                .orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapToDto)
                .orElse(null);
    }

    @Override
    public UserResponseDto updateUserByEmail(String email, UserRequestDto userDto) {
        return userRepository.findByEmail(email)
                .map(existing -> {
                    existing.setName(userDto.getName());
                    // Email değiştirilemez, mevcut email korunur
                    existing.setPhone(userDto.getPhone());
                    existing.setAddress(userDto.getAddress());
                    // Password güncellenirse hashlenir
                    if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                        existing.setPassword(passwordEncoder.encode(userDto.getPassword()));
                    }
                    // Role güncellenmez, mevcut role korunur
                    return mapToDto(userRepository.save(existing));
                })
                .orElse(null);
    }
}
