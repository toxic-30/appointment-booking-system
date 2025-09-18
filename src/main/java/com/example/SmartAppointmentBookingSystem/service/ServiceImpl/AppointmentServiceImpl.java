package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.example.SmartAppointmentBookingSystem.dto.appointment.AppointmentRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.appointment.AppointmentResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.Appointment;
import com.example.SmartAppointmentBookingSystem.entity.ProvidedService;
import com.example.SmartAppointmentBookingSystem.entity.Tenant;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.enums.AppointmentStatus;
import com.example.SmartAppointmentBookingSystem.repository.AppointmentRepository;
import com.example.SmartAppointmentBookingSystem.repository.ProvidedServiceRepository;
import com.example.SmartAppointmentBookingSystem.repository.TenantRepository;
import com.example.SmartAppointmentBookingSystem.repository.UserRepository;
import com.example.SmartAppointmentBookingSystem.service.AppointmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepo;
    private final UserRepository userRepo;
    private final TenantRepository tenantRepo;
    private final ProvidedServiceRepository serviceRepo;

    @Override
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO requestDTO) {
        
        User provider = userRepo.findById(requestDTO.getProviderId())
        .orElseThrow(() -> new RuntimeException("Provider not found with id: " + requestDTO.getProviderId()));
        User customer = userRepo.findById(requestDTO.getCustomerId())
        .orElseThrow(() -> new RuntimeException("Customer not found with id: " + requestDTO.getCustomerId()));
        ProvidedService service = serviceRepo.findById(requestDTO.getServiceId())
        .orElseThrow(() -> new RuntimeException("Service not found with id: " + requestDTO.getServiceId()));
        Tenant tenant = tenantRepo.findById(requestDTO.getTenantId())
         .orElseThrow(() -> new RuntimeException("Tenant not found with id: " + requestDTO.getTenantId()));

        Appointment appointment = new Appointment();
        appointment.setProvider(provider);
        appointment.setCustomer(customer);
        appointment.setTenant(tenant);
        appointment.setService(service);
        appointment.setAppointmentTime(requestDTO.getAppointmentTime());
        appointment.setNotes(requestDTO.getNotes());
        appointment.setStatus(AppointmentStatus.PENDING);

        return toResponseDTO(appointmentRepo.save(appointment));
    }

    @Override
    public AppointmentResponseDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        return toResponseDTO(appointment);
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByCustomer(Long customerId) {
        return appointmentRepo.findByCustomerId(customerId)
                .stream().map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByProvider(Long providerId) {
        return appointmentRepo.findByProviderId(providerId)
                .stream().map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByTenant(Long tenantId) {
        return appointmentRepo.findByTenantId(tenantId)
                .stream().map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponseDTO updateAppointmentStatus(Long id, String status) {
        Appointment appointment = appointmentRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
            try {
                appointment.setStatus(AppointmentStatus.valueOf(status.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid status: " + status);
            }
        return toResponseDTO(appointmentRepo.save(appointment));
    }

    @Override
    public void deleteAppointment(Long id) {
        if (!appointmentRepo.existsById(id)) {
        throw new RuntimeException("Appointment not found");
    }
    appointmentRepo.deleteById(id);
    }

    private AppointmentResponseDTO toResponseDTO(Appointment appointment) {
        AppointmentResponseDTO dto = new AppointmentResponseDTO();
        dto.setId(appointment.getId());
        dto.setStatus(appointment.getStatus());
        dto.setProviderName(appointment.getProvider() != null ? appointment.getProvider().getName() : null);
        dto.setCustomerName(appointment.getCustomer() != null ? appointment.getCustomer().getName() : null);
        dto.setServiceName(appointment.getService() != null ? appointment.getService().getName() : null);
        dto.setTenantName(appointment.getTenant() != null ? appointment.getTenant().getName() : null);
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setNotes(appointment.getNotes());
        dto.setCreatedAt(appointment.getCreatedAt());
        dto.setUpdatedAt(appointment.getUpdatedAt());
    return dto;
    }
}
