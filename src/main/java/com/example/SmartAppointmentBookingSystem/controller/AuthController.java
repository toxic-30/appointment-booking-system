package com.example.SmartAppointmentBookingSystem.controller;

import com.example.SmartAppointmentBookingSystem.dto.auth.LoginRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.auth.LoginResponseDTO;
import com.example.SmartAppointmentBookingSystem.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .orElse("USER");

        String token = jwtUtil.generateToken(request.getEmail(), role);

        return ResponseEntity.ok(new LoginResponseDTO(token, role));
    }
}
