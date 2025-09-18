package com.example.SmartAppointmentBookingSystem.dto.appointment;

import java.time.LocalDateTime;
import com.example.SmartAppointmentBookingSystem.enums.AppointmentStatus;
import lombok.Data;

@Data
public class AppointmentResponseDTO {
    private Long id;
    private AppointmentStatus status;
    private String providerName;
    private String customerName;
    private String serviceName;
    private String tenantName;
    private LocalDateTime appointmentTime;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
