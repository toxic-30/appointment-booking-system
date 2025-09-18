package com.example.SmartAppointmentBookingSystem.dto.tenant;
import lombok.Data;

@Data
public class TenantRequestDTO {
    private String name;
    private String address;
    private String contactNumber;
    private String email;
}
