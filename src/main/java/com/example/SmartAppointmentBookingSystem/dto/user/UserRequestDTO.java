package com.example.SmartAppointmentBookingSystem.dto.user;
import lombok.Data;
import com.example.SmartAppointmentBookingSystem.enums.UserRole;

@Data
public class UserRequestDTO {
    private String name;
    private String email;
    private String password;   // plain text, will be hashed in service layer
    private UserRole role;     // e.g., CUSTOMER, PROVIDER, ADMIN
    private Long tenantId;
    // If provider is creating a new tenant, pass these
    private String tenantName;
    private String tenantAddress;
    private String tenantContactNumber;
    private String tenantEmail;
}
