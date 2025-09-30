package com.example.SmartAppointmentBookingSystem.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
}