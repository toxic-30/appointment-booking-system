package com.example.SmartAppointmentBookingSystem.dto.tenant;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class TenantRequestDTO {
    @NotBlank
    private String name;

    private String address;

    @NotBlank
    private String contactNumber;

    @NotBlank
    @Email
    private String email;
}
