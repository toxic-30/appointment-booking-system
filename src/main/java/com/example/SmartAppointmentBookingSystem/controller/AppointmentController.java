package com.example.SmartAppointmentBookingSystem.controller;

import com.example.SmartAppointmentBookingSystem.dto.appointment.AppointmentRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.appointment.AppointmentResponseDTO;
import com.example.SmartAppointmentBookingSystem.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public AppointmentResponseDTO createAppointment(@RequestBody AppointmentRequestDTO requestDTO) {
        return appointmentService.createAppointment(requestDTO);
    }

    @GetMapping("/{id}")
    public AppointmentResponseDTO getAppointment(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<AppointmentResponseDTO> getAppointmentsByCustomer(@PathVariable Long customerId) {
        return appointmentService.getAppointmentsByCustomer(customerId);
    }

    @GetMapping("/provider/{providerId}")
    public List<AppointmentResponseDTO> getAppointmentsByProvider(@PathVariable Long providerId) {
        return appointmentService.getAppointmentsByProvider(providerId);
    }

    @GetMapping("/tenant/{tenantId}")
    public List<AppointmentResponseDTO> getAppointmentsByTenant(@PathVariable Long tenantId) {
        return appointmentService.getAppointmentsByTenant(tenantId);
    }

    @PutMapping("/{id}/status")
    public AppointmentResponseDTO updateStatus(@PathVariable Long id, @RequestParam String status) {
        return appointmentService.updateAppointmentStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
    }
}
