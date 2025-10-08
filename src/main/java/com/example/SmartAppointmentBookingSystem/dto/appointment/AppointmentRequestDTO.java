package com.example.SmartAppointmentBookingSystem.dto.appointment;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

@Data
public class AppointmentRequestDTO {
    @NotNull
    private Long providerId;
    @NotNull
    private Long customerId;
    @NotNull
    private Long serviceId;
    @NotNull
    private Long tenantId;
    @NotNull
    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentTime;
    private String notes;
}
