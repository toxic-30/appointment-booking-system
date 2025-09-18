package com.example.SmartAppointmentBookingSystem.dto.user;
import lombok.Data;
import com.example.SmartAppointmentBookingSystem.enums.UserRole;

@Data
public class UserResponseDTO {
    private String userCode;   
    private String name;
    private String email;
    private UserRole role;
    private String tenantName;
    private Long tenantId;
}
