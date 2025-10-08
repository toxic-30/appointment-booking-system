package com.example.SmartAppointmentBookingSystem.controller;

import com.example.SmartAppointmentBookingSystem.dto.appointment.AppointmentRequestDTO;
import jakarta.validation.Valid;
import com.example.SmartAppointmentBookingSystem.dto.appointment.AppointmentResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.enums.UserRole;
import com.example.SmartAppointmentBookingSystem.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/createAppointment")
    public AppointmentResponseDTO createAppointment(@Valid @RequestBody AppointmentRequestDTO appointmentRequestDTO,
                                                    @AuthenticationPrincipal User currentUser) {
        if (currentUser.getRole() != UserRole.CUSTOMER && currentUser.getRole() != UserRole.PROVIDER) {
            throw new RuntimeException("Admin are not allowed to create appointments"); }
        return appointmentService.createAppointment(appointmentRequestDTO, currentUser); 
    }

    @GetMapping("/getAppointment/{id}")
    public AppointmentResponseDTO getAppointment(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return appointmentService.getAppointmentById(id, currentUser);
    }

    @GetMapping("/customer/{customerId}")
    public List<AppointmentResponseDTO> getAppointmentsByCustomer(@PathVariable Long customerId,
                                                                  @AuthenticationPrincipal User currentUser) {
        if (currentUser.getRole() != UserRole.CUSTOMER && currentUser.getRole() != UserRole.ADMIN){
            throw new RuntimeException("Only customer can access appointment using customerId");
        }
        return appointmentService.getAppointmentsByCustomer(customerId, currentUser);                                                      
    }

    @GetMapping("/provider/{providerId}")
    public List<AppointmentResponseDTO> getAppointmentsByProvider(@PathVariable Long providerId,
                                                           @AuthenticationPrincipal User currentUser) {
        if (currentUser.getRole() != UserRole.PROVIDER && currentUser.getRole() != UserRole.ADMIN){
            throw new RuntimeException("Only provider can access appointment using providerId");   
        }                                                     
        return appointmentService.getAppointmentsByProvider(providerId, currentUser);   
    }

    @GetMapping("/tenant/{tenantId}")
    public List<AppointmentResponseDTO> getAppointmentsByTenant(@PathVariable Long tenantId,
                                                                @AuthenticationPrincipal User currentUser) {
       if (currentUser.getRole() != UserRole.ADMIN){
            throw new RuntimeException("Only admin can access appointment using tenantId");   
        }                                                  
       return appointmentService.getAppointmentsByTenant(tenantId, currentUser);
    }
    @PutMapping("/updateStatus/{id}")
    public AppointmentResponseDTO updateStatus(@PathVariable Long id, @RequestParam String status,
                                               @AuthenticationPrincipal User currentUser) {
        if (currentUser.getRole() != UserRole.PROVIDER && currentUser.getRole() != UserRole.ADMIN){
            throw new RuntimeException("Only Provider & Admin can update appointment.");   
        }
        return appointmentService.updateAppointmentStatus(id, status, currentUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        appointmentService.deleteAppointment(id, currentUser);
        return ResponseEntity.noContent().build();
    }

}
