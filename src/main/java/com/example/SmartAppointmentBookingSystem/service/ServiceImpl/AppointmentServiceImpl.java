package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
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
import com.example.SmartAppointmentBookingSystem.enums.UserRole;
import com.example.SmartAppointmentBookingSystem.exception.ResourceNotFoundException;
import com.example.SmartAppointmentBookingSystem.repository.AppointmentRepository;
import com.example.SmartAppointmentBookingSystem.repository.ProvidedServiceRepository;
import com.example.SmartAppointmentBookingSystem.repository.UserRepository;
import com.example.SmartAppointmentBookingSystem.service.AppointmentService;
import com.example.SmartAppointmentBookingSystem.service.NotificationService;
import com.example.SmartAppointmentBookingSystem.util.ReminderUtil;
import com.example.SmartAppointmentBookingSystem.util.TimeUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepo;
    private final UserRepository userRepo;
    private final ProvidedServiceRepository serviceRepo;
    private final NotificationService notificationService;
    private final ReminderUtil reminderUtil;

    @Override
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto, User currentUser) {
        // Fetch all related entities
        User provider = fetchProvider(dto.getProviderId());
        User customer = resolveCustomer(dto, currentUser);
        ProvidedService service = fetchService(dto.getServiceId());
        Tenant tenant = provider.getTenant();

        // Access control
        checkTenantAccess(currentUser, tenant);

        // Time validation
        LocalDateTime startTime = dto.getAppointmentTime();
        LocalDateTime endTime = startTime.plusMinutes(service.getDurationMinutes());
        validateAppointmentTime(startTime);
        checkProviderAvailability(provider.getId(), startTime, endTime);

        // Create appointment
        Appointment appointment = buildAppointment(dto, provider, customer, tenant, service);
        Appointment savedAppointment = appointmentRepo.save(appointment);

        // Notifications
        sendImmediateNotification(savedAppointment);
        scheduleReminder(savedAppointment);

        return toResponseDTO(savedAppointment);
    }

    private User fetchProvider(Long id) {
        User provider =  userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + id));
        if(provider.getRole() != UserRole.PROVIDER) {
            throw new IllegalArgumentException("User with id " + id + " is not a provider");
        }
        return provider;        
    }
    private User resolveCustomer(AppointmentRequestDTO dto, User currentUser) {
        if (currentUser.getRole() == UserRole.CUSTOMER)
            return currentUser;
        return userRepo.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + dto.getCustomerId()));
    }

    private ProvidedService fetchService(Long serviceId) {
        return serviceRepo.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + serviceId));
    }
    private void checkTenantAccess(User currentUser, Tenant tenant) {
        if (currentUser.getRole() == UserRole.PROVIDER) {
            if (currentUser.getTenant() == null || !currentUser.getTenant().getId().equals(tenant.getId())) {
                throw new AccessDeniedException("Cannot create appointment outside your tenant");
            }
        } 
    }
    private void validateAppointmentTime(LocalDateTime startTime) {
        if (startTime.isBefore(TimeUtil.now())) {
            throw new IllegalArgumentException("Cannot book an appointment in the past.");
        }
    }
    private void checkProviderAvailability(Long providerId, LocalDateTime start, LocalDateTime end) {
        boolean hasOverlap = appointmentRepo.existsByProviderIdAndAppointmentTimeBetween(providerId, start, end);
        if (hasOverlap) {
            throw new IllegalArgumentException("Provider already has an appointment during this time.");
        }
    }
    private Appointment buildAppointment(AppointmentRequestDTO dto, User provider, User customer,
        Tenant tenant, ProvidedService service) {
        Appointment appointment = new Appointment();
        appointment.setProvider(provider);
        appointment.setCustomer(customer);
        appointment.setTenant(tenant);
        appointment.setService(service);
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setNotes(dto.getNotes());
        appointment.setStatus(AppointmentStatus.BOOKED);
        return appointment;
    }
    @Async
    private void sendImmediateNotification(Appointment appointment) {
        NotificationRequestDTO immediate = new NotificationRequestDTO();
        immediate.setRecipientId(appointment.getCustomer().getId());
        immediate.setAppointmentId(appointment.getId());
        immediate.setMessage("Your appointment is booked for " + TimeUtil.format(appointment.getAppointmentTime())
                + " with " + appointment.getProvider().getName() + " for service: "
                + appointment.getService().getName());
        immediate.setType(NotificationType.EMAIL);
        immediate.setEvent(NotificationEvent.APPOINTMENT_BOOKED);

        try {
            notificationService.sendNotification(immediate);
        } catch (Exception e) {
            log.error("Failed to send booking confirmation for appointment {}: {}", appointment.getId(), e.getMessage(),
                    e);
        }
    }
    @Async
    private void scheduleReminder(Appointment appointment) {
        int offsetHours = Math.max(0, reminderUtil.getReminderOffsetHours());
        LocalDateTime reminderTime = appointment.getAppointmentTime().minusHours(offsetHours);
        if (reminderTime.isAfter(TimeUtil.now())) {
            try {
                NotificationRequestDTO reminderRequest = new NotificationRequestDTO();
                reminderRequest.setRecipientId(appointment.getCustomer().getId());
                reminderRequest.setAppointmentId(appointment.getId());
                reminderRequest.setMessage(
                        "Reminder: Your appointment is at " + TimeUtil.format(appointment.getAppointmentTime())
                                + " with " + appointment.getProvider().getName() + " for service: "
                                + appointment.getService().getName());
                reminderRequest.setType(NotificationType.EMAIL);
                reminderRequest.setEvent(NotificationEvent.APPOINTMENT_REMINDER);
                reminderRequest.setScheduledAt(reminderTime);
                notificationService.scheduleNotification(reminderRequest);
            } catch (Exception e) {
                log.error("Failed to schedule reminder for appointment {}: {}", appointment.getId(), e.getMessage(), e);
            }
        }
    }
    @Override
    public AppointmentResponseDTO getAppointmentById(Long id, User currentUser) {
        Appointment appointment = appointmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        enforceAccess(currentUser, appointment);
        return toResponseDTO(appointment);
    }
    @Override
    public List<AppointmentResponseDTO> getAppointmentsByCustomer(Long customerId, User currentUser) {
        if (currentUser.getRole() == UserRole.CUSTOMER && !currentUser.getId().equals(customerId)) {
            throw new AccessDeniedException("Cannot view other customers' appointments");
        }
        return appointmentRepo.findByCustomerId(customerId).stream()
                .map(this::toResponseDTO).collect(Collectors.toList());
    }
    @Override
    public List<AppointmentResponseDTO> getAppointmentsByProvider(Long providerId, User currentUser) {
        if (currentUser.getRole() == UserRole.PROVIDER && !currentUser.getId().equals(providerId)) {
            throw new AccessDeniedException("Cannot view other providers' appointments");
        }
        return appointmentRepo.findByProviderId(providerId).stream()
                .map(this::toResponseDTO).collect(Collectors.toList());
    }
    @Override
    public List<AppointmentResponseDTO> getAppointmentsByTenant(Long tenantId, User currentUser) {
        if (currentUser.getRole() == UserRole.ADMIN) {
            return appointmentRepo.findByTenantId(tenantId)
                    .stream()
                    .map(this::toResponseDTO)
                    .toList();
        }
        if (currentUser.getRole() == UserRole.PROVIDER) {
            if (currentUser.getTenant() != null &&
                    currentUser.getTenant().getId().equals(tenantId)) {
                return appointmentRepo.findByTenantId(tenantId)
                        .stream()
                        .map(this::toResponseDTO)
                        .toList();
            } else {
                throw new AccessDeniedException("Provider can only view their own tenant appointments");
            }
        }
        throw new AccessDeniedException("Only admin or provider can view tenant appointments");
    }

    @Override
    public AppointmentResponseDTO updateAppointmentStatus(Long id, String status, User currentUser) {
        Appointment appointment = appointmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        enforceAccess(currentUser, appointment);

        try {
            appointment.setStatus(AppointmentStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Invalid status: " + status);
        }

        return toResponseDTO(appointmentRepo.save(appointment));
    }

    @Override
    public void deleteAppointment(Long id, User currentUser) {
        Appointment appointment = appointmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (currentUser.getRole() == UserRole.ADMIN ||
                (currentUser.getRole() == UserRole.CUSTOMER
                        && appointment.getCustomer().getId().equals(currentUser.getId()))
                ||
                (currentUser.getRole() == UserRole.PROVIDER
                        && appointment.getProvider().getId().equals(currentUser.getId()))) {
            // Soft delete
            appointment.setStatus(AppointmentStatus.CANCELLED);
            appointmentRepo.save(appointment);
        } else {
            throw new AccessDeniedException("You are not allowed to delete/cancel this appointment");
        }
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

    private void enforceAccess(User currentUser, Appointment appointment) {
        if (currentUser.getRole() == UserRole.ADMIN)
            return;

        if (currentUser.getRole() == UserRole.CUSTOMER &&
                !appointment.getCustomer().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Cannot access other customers' appointments");
        }

        if (currentUser.getRole() == UserRole.PROVIDER &&
                !appointment.getProvider().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Cannot access other providers' appointments");
        }
    }
}