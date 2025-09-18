package com.example.SmartAppointmentBookingSystem.service;

import java.util.List;
import com.example.SmartAppointmentBookingSystem.dto.appointment.AppointmentRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.appointment.AppointmentResponseDTO;

public interface AppointmentService {

    AppointmentResponseDTO createAppointment(AppointmentRequestDTO requestDTO);
    AppointmentResponseDTO getAppointmentById(Long id);
    List<AppointmentResponseDTO> getAppointmentsByCustomer(Long customerId);
    List<AppointmentResponseDTO> getAppointmentsByProvider(Long providerId);
    List<AppointmentResponseDTO> getAppointmentsByTenant(Long tenantId);
    AppointmentResponseDTO updateAppointmentStatus(Long id, String status);
    void deleteAppointment(Long id);

}
