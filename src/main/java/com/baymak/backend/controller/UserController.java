package com.baymak.backend.controller;

import com.baymak.backend.dto.UserRequestDto;
import com.baymak.backend.dto.UserResponseDto;
import com.baymak.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management operations")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Tüm kullanıcıları listele", description = "Sistemdeki tüm kullanıcıları listeler. JWT token gereklidir.")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Kullanıcı detayı", description = "Belirli bir kullanıcının detaylarını getirir. JWT token gereklidir.")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Yeni kullanıcı oluştur", description = "Yeni bir kullanıcı oluşturur. Email unique olmalıdır. JWT token gereklidir.")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userDto) {
        UserResponseDto created = userService.createUser(userDto);
        return ResponseEntity.created(URI.create("/api/users/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Kullanıcı güncelle", description = "Mevcut bir kullanıcının bilgilerini günceller. JWT token gereklidir.")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDto userDto) {
        UserResponseDto updated = userService.updateUser(id, userDto);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Kullanıcı sil", description = "Belirli bir kullanıcıyı siler. JWT token gereklidir.")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
