package com.example.SmartAppointmentBookingSystem.dto.user;
import lombok.Data;
import com.example.SmartAppointmentBookingSystem.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
public class UserRequestDTO {
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;   // plain text, will be hashed in service layer
    @NotNull
    private UserRole role;     // e.g., CUSTOMER, PROVIDER, ADMIN
    private Long tenantId;
    // If provider is creating a new tenant, pass these
    private String tenantName;
    private String tenantAddress;
    @Pattern(regexp = "\\d{10}", message = "Tenant contact number must be exactly 10 digits")
    private String tenantContactNumber;
    @Email
    private String tenantEmail;
}
