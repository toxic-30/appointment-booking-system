package com.example.SmartAppointmentBookingSystem.controller;

import com.example.SmartAppointmentBookingSystem.dto.user.UserRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.user.UserResponseDTO;
import com.example.SmartAppointmentBookingSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
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
    @PostMapping("/addUser")
    public ResponseEntity<UserResponseDTO> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userService.addUser(userRequestDTO));
    }
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO userRequestDTO
    ) {
        return ResponseEntity.ok(userService.updateUser(id, userRequestDTO));
    }
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

