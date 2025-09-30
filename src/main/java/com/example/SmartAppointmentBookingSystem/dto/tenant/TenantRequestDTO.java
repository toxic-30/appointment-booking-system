package com.example.SmartAppointmentBookingSystem.dto.tenant;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
public class TenantRequestDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "Tenant contact number must be exactly 10 digits")
    private String contactNumber;
    @NotBlank
    @Email
    private String email;
}
