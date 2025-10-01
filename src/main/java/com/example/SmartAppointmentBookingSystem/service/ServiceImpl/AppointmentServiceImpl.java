package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.example.SmartAppointmentBookingSystem.dto.appointment.AppointmentRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.appointment.AppointmentResponseDTO;
import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationRequestDTO;
import com.example.SmartAppointmentBookingSystem.entity.Appointment;
import com.example.SmartAppointmentBookingSystem.entity.ProvidedService;
import com.example.SmartAppointmentBookingSystem.entity.Tenant;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.enums.AppointmentStatus;
import com.example.SmartAppointmentBookingSystem.enums.NotificationEvent;
import com.example.SmartAppointmentBookingSystem.enums.NotificationType;
import com.example.SmartAppointmentBookingSystem.exception.ResourceNotFoundException;
import com.example.SmartAppointmentBookingSystem.repository.AppointmentRepository;
import com.example.SmartAppointmentBookingSystem.repository.ProvidedServiceRepository;
import com.example.SmartAppointmentBookingSystem.repository.TenantRepository;
import com.example.SmartAppointmentBookingSystem.repository.UserRepository;
import com.example.SmartAppointmentBookingSystem.service.AppointmentService;
import com.example.SmartAppointmentBookingSystem.service.NotificationService;

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
    private final NotificationService notificationService;

    @Override
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO requestDTO) {

        // Fetch related entities
        User provider = userRepo.findById(requestDTO.getProviderId())
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + requestDTO.getProviderId()));
        User customer = userRepo.findById(requestDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + requestDTO.getCustomerId()));
        ProvidedService service = serviceRepo.findById(requestDTO.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + requestDTO.getServiceId()));
        Tenant tenant = tenantRepo.findById(requestDTO.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + requestDTO.getTenantId()));

        // Create appointment
        Appointment appointment = new Appointment();
            appointment.setProvider(provider);
            appointment.setCustomer(customer);
            appointment.setTenant(tenant);
            appointment.setService(service);
            appointment.setAppointmentTime(requestDTO.getAppointmentTime());
            appointment.setNotes(requestDTO.getNotes());
            appointment.setStatus(AppointmentStatus.PENDING);
        Appointment savedAppointment = appointmentRepo.save(appointment);

        // 1️ Send immediate notification
        try {
            NotificationRequestDTO notificationRequest = new NotificationRequestDTO();
            notificationRequest.setRecipientId(customer.getId());
            notificationRequest.setAppointmentId(savedAppointment.getId());
            notificationRequest.setMessage("Your appointment has been booked successfully.");
            notificationRequest.setType(NotificationType.EMAIL);
            notificationRequest.setEvent(NotificationEvent.APPOINTMENT_BOOKED);
            notificationService.sendNotification(notificationRequest);
        } catch (Exception ex) {
            System.err.println("Failed to send booking notification for appointment " + savedAppointment.getId() + ": " + ex.getMessage());
        }

        // 2️ Schedule reminder 1 day before appointment
        if (savedAppointment.getAppointmentTime() != null) {
            LocalDateTime reminderTime = savedAppointment.getAppointmentTime().minusDays(1);
            if (reminderTime.isAfter(LocalDateTime.now())) {
                try {
                    NotificationRequestDTO reminderRequest = new NotificationRequestDTO();
                    reminderRequest.setRecipientId(customer.getId());
                    reminderRequest.setAppointmentId(savedAppointment.getId());
                    reminderRequest.setMessage("Reminder: Your appointment is tomorrow at " + savedAppointment.getAppointmentTime());
                    reminderRequest.setType(NotificationType.EMAIL);
                    reminderRequest.setEvent(NotificationEvent.APPOINTMENT_REMINDER);
                    reminderRequest.setScheduledAt(reminderTime);
                    notificationService.scheduleNotification(reminderRequest);
                } catch (Exception ex) {
                    System.err.println("Failed to schedule reminder for appointment " + savedAppointment.getId() + ": " + ex.getMessage());
                }
            } else {
                System.out.println("Skipping reminder scheduling as the time is already passed for appointment " + savedAppointment.getId());
            }
        }

        return toResponseDTO(savedAppointment);
    }

    @Override
    public AppointmentResponseDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
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
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        try {
            appointment.setStatus(AppointmentStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Invalid status: " + status);
        }
        return toResponseDTO(appointmentRepo.save(appointment));
    }

    @Override
    public void deleteAppointment(Long id) {
        if (!appointmentRepo.existsById(id)) {
            throw new ResourceNotFoundException("Appointment not found with id: " + id);
        }
        appointmentRepo.deleteById(id);
    }

    // Convert Appointment entity to DTO
    private AppointmentResponseDTO toResponseDTO(Appointment appointment) {
        AppointmentResponseDTO dto = new AppointmentResponseDTO();
        dto.setId(appointment.getId());
        dto.setProviderName(appointment.getProvider().getName());
        dto.setCustomerName(appointment.getCustomer().getName());
        dto.setServiceName(appointment.getService().getName());
        dto.setTenantName(appointment.getTenant().getName());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setNotes(appointment.getNotes());
        dto.setStatus(appointment.getStatus());
        dto.setCreatedAt(appointment.getCreatedAt());
        dto.setUpdatedAt(appointment.getUpdatedAt());
        return dto;
    }
}