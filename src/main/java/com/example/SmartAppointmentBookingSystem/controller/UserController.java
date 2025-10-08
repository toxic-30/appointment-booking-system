package com.example.SmartAppointmentBookingSystem.controller;

import com.example.SmartAppointmentBookingSystem.dto.user.UserRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.user.UserResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.enums.UserRole;
import com.example.SmartAppointmentBookingSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/addUser")
    public ResponseEntity<UserResponseDTO> addUser(
            @Valid @RequestBody UserRequestDTO userRequestDTO,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(userService.addUser(userRequestDTO, currentUser));
    }

    // Get all users (admin only)
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(
            @AuthenticationPrincipal User currentUser) {
        if (currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Only admin can view all users");
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{userCode}")
    public ResponseEntity<UserResponseDTO> getUserByUserCode(@PathVariable String userCode) {
        return userService.getUserByUserCode(userCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO userRequestDTO,
            @AuthenticationPrincipal User currentUser) {

        if (currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Only admin can update users");
        }
        return ResponseEntity.ok(userService.updateUser(id, userRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        if (currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Only admin can delete users");
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
