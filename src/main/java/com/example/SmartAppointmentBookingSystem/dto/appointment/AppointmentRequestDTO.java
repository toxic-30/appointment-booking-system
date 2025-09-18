package com.example.SmartAppointmentBookingSystem.dto.appointment;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AppointmentRequestDTO {
    private Long providerId;
    private Long customerId;
    private Long serviceId;
    private Long tenantId;
    private LocalDateTime appointmentTime;
    private String notes;
}