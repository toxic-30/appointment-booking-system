package com.example.SmartAppointmentBookingSystem.dto.appointment;
import java.time.LocalDateTime;

import com.example.SmartAppointmentBookingSystem.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class AppointmentResponseDTO {
    private Long id;
    private AppointmentStatus status;
    private String providerName;
    private String customerName;
    private String serviceName;
    private String tenantName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Kolkata")
    private LocalDateTime  appointmentTime; 
    private String notes;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Kolkata")
    private LocalDateTime  createdAt;     
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Kolkata")
    private LocalDateTime  updatedAt;       
}
