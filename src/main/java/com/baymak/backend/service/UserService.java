package com.baymak.backend.service;

import com.baymak.backend.dto.UserRequestDto;
import com.baymak.backend.dto.UserResponseDto;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserResponseDto> getAllUsers();

    Optional<UserResponseDto> getUserById(Long id);

    UserResponseDto createUser(UserRequestDto userDto);

    UserResponseDto updateUser(Long id, UserRequestDto userDto);

    void deleteUser(Long id);
}
