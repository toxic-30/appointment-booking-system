package com.example.SmartAppointmentBookingSystem.service;

import java.util.List;
import com.example.SmartAppointmentBookingSystem.dto.appointment.AppointmentRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.appointment.AppointmentResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.User;

public interface AppointmentService {

    AppointmentResponseDTO createAppointment(AppointmentRequestDTO requestDTO,User currentUser);
    AppointmentResponseDTO getAppointmentById(Long id,User currentUser);
    List<AppointmentResponseDTO> getAppointmentsByCustomer(Long customerId,User currentUser);
    List<AppointmentResponseDTO> getAppointmentsByProvider(Long providerId,User currentUser);
    List<AppointmentResponseDTO> getAppointmentsByTenant(Long tenantId, User currentUser);
    AppointmentResponseDTO updateAppointmentStatus(Long id, String status,User currentUser);
    void deleteAppointment(Long id,User currentUser);

}
