package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import com.example.SmartAppointmentBookingSystem.dto.appointment.AppointmentRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.appointment.AppointmentResponseDTO;
import com.example.SmartAppointmentBookingSystem.dto.notification.NotificationRequestDTO;
import com.example.SmartAppointmentBookingSystem.entity.*;
import com.example.SmartAppointmentBookingSystem.enums.AppointmentStatus;
import com.example.SmartAppointmentBookingSystem.exception.ResourceNotFoundException;
import com.example.SmartAppointmentBookingSystem.repository.*;
import com.example.SmartAppointmentBookingSystem.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AppointmentServiceImplTest {

    @Mock AppointmentRepository appointmentRepo;
    @Mock UserRepository userRepo;
    @Mock TenantRepository tenantRepo;
    @Mock ProvidedServiceRepository serviceRepo;
    @Mock NotificationService notificationService;

    @InjectMocks AppointmentServiceImpl service;

    @Test
    void createAppointment_success() {
        AppointmentRequestDTO req = new AppointmentRequestDTO();
        req.setProviderId(1L);
        req.setCustomerId(2L);
        req.setServiceId(3L);
        req.setTenantId(4L);
        req.setAppointmentTime(LocalDateTime.now().plusDays(2));
        req.setNotes("Test");

        User provider = new User(); provider.setId(1L); provider.setName("Provider");
        User customer = new User(); customer.setId(2L); customer.setName("Customer");
        ProvidedService providedService = new ProvidedService(); providedService.setId(3L); providedService.setName("Service");
        Tenant tenant = new Tenant(); tenant.setId(4L); tenant.setName("Tenant");

        when(userRepo.findById(1L)).thenReturn(Optional.of(provider));
        when(userRepo.findById(2L)).thenReturn(Optional.of(customer));
        when(serviceRepo.findById(3L)).thenReturn(Optional.of(providedService));
        when(tenantRepo.findById(4L)).thenReturn(Optional.of(tenant));

        Appointment appointment = new Appointment();
        appointment.setProvider(provider);
        appointment.setCustomer(customer);
        appointment.setService(providedService);
        appointment.setTenant(tenant);
        appointment.setAppointmentTime(req.getAppointmentTime());
        appointment.setNotes(req.getNotes());
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setId(10L);

        when(appointmentRepo.save(any())).thenReturn(appointment);

        doNothing().when(notificationService).sendNotification(any(NotificationRequestDTO.class));
        doNothing().when(notificationService).scheduleNotification(any(NotificationRequestDTO.class));

        AppointmentResponseDTO dto = service.createAppointment(req);

        assertEquals(10L, dto.getId());
        assertEquals("Provider", dto.getProviderName());
        assertEquals("Customer", dto.getCustomerName());
        assertEquals("Service", dto.getServiceName());
        assertEquals("Tenant", dto.getTenantName());
    }

    @Test
    void createAppointment_notificationFailure_doesNotFailCreation() {
        AppointmentRequestDTO req = new AppointmentRequestDTO();
        req.setProviderId(1L);
        req.setCustomerId(2L);
        req.setServiceId(3L);
        req.setTenantId(4L);
        req.setAppointmentTime(LocalDateTime.now().plusDays(2));
        req.setNotes("Test");

        User provider = new User(); provider.setId(1L); provider.setName("Provider");
        User customer = new User(); customer.setId(2L); customer.setName("Customer");
        ProvidedService providedService = new ProvidedService(); providedService.setId(3L); providedService.setName("Service");
        Tenant tenant = new Tenant(); tenant.setId(4L); tenant.setName("Tenant");

        when(userRepo.findById(1L)).thenReturn(Optional.of(provider));
        when(userRepo.findById(2L)).thenReturn(Optional.of(customer));
        when(serviceRepo.findById(3L)).thenReturn(Optional.of(providedService));
        when(tenantRepo.findById(4L)).thenReturn(Optional.of(tenant));

        Appointment appointment = new Appointment();
        appointment.setProvider(provider);
        appointment.setCustomer(customer);
        appointment.setService(providedService);
        appointment.setTenant(tenant);
        appointment.setAppointmentTime(req.getAppointmentTime());
        appointment.setNotes(req.getNotes());
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setId(11L);
        
        when(appointmentRepo.save(any())).thenReturn(appointment);

        doThrow(new RuntimeException("email failure")).when(notificationService).sendNotification(any(NotificationRequestDTO.class));
        doThrow(new RuntimeException("rabbit failure")).when(notificationService).scheduleNotification(any(NotificationRequestDTO.class));

        AppointmentResponseDTO dto = service.createAppointment(req);

        assertEquals(11L, dto.getId());
        // ensure that despite notification failures, appointment creation succeeds
        verify(notificationService, times(1)).sendNotification(any());
        verify(notificationService, times(1)).scheduleNotification(any());
    }

    @Test
    void getAppointmentById_found() {
        Appointment appointment = new Appointment();
        appointment.setId(1L); // usually JPA will generate this automatically
        appointment.setProvider(new User());
        appointment.setCustomer(new User());
        appointment.setService(new ProvidedService());
        appointment.setTenant(new Tenant());
        appointment.setAppointmentTime(LocalDateTime.now());
        appointment.setStatus(AppointmentStatus.PENDING);
        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appointment));
        AppointmentResponseDTO dto = service.getAppointmentById(1L);
        assertEquals(1L, dto.getId());
    }

    @Test
    void getAppointmentById_notFound() {
        when(appointmentRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getAppointmentById(1L));
    }

    @Test
    void updateAppointmentStatus_valid() {
        Appointment appointment = new Appointment();
        appointment.setId(1L); // usually JPA will generate this automatically
        appointment.setProvider(new User());
        appointment.setCustomer(new User());
        appointment.setService(new ProvidedService());
        appointment.setTenant(new Tenant());
        appointment.setAppointmentTime(LocalDateTime.now());
        appointment.setStatus(AppointmentStatus.PENDING);
        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        AppointmentResponseDTO dto = service.updateAppointmentStatus(1L, "BOOKED");
        assertEquals(AppointmentStatus.BOOKED, dto.getStatus());
    }

    @Test
    void updateAppointmentStatus_invalid() {
        Appointment appointment = new Appointment();
        appointment.setId(1L); // usually JPA will generate this automatically
        appointment.setProvider(new User());
        appointment.setCustomer(new User());
        appointment.setService(new ProvidedService());
        appointment.setTenant(new Tenant());
        appointment.setAppointmentTime(LocalDateTime.now());
        appointment.setStatus(AppointmentStatus.PENDING);
        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appointment));
        assertThrows(ResourceNotFoundException.class, () -> service.updateAppointmentStatus(1L, "INVALID"));
    }

    @Test
    void deleteAppointment_found() {
        when(appointmentRepo.existsById(1L)).thenReturn(true);
        doNothing().when(appointmentRepo).deleteById(1L);
        assertDoesNotThrow(() -> service.deleteAppointment(1L));
    }

    @Test
    void deleteAppointment_notFound() {
        when(appointmentRepo.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.deleteAppointment(1L));
    }

    @Test
    void getAppointmentsByCustomer() {
        Appointment appointment = new Appointment();
        appointment.setId(1L); // usually JPA will generate this automatically
        appointment.setProvider(new User());
        appointment.setCustomer(new User());
        appointment.setService(new ProvidedService());
        appointment.setTenant(new Tenant());
        appointment.setAppointmentTime(LocalDateTime.now());
        when(appointmentRepo.findByCustomerId(2L)).thenReturn(List.of(appointment));
        List<AppointmentResponseDTO> dtos = service.getAppointmentsByCustomer(2L);
        assertEquals(1, dtos.size());
    }

    @Test
    void getAppointmentsByProvider() {
        Appointment appointment = new Appointment();
        appointment.setId(1L); // usually JPA will generate this automatically
        appointment.setProvider(new User());
        appointment.setCustomer(new User());
        appointment.setService(new ProvidedService());
        appointment.setTenant(new Tenant());
        appointment.setAppointmentTime(LocalDateTime.now());
        when(appointmentRepo.findByProviderId(1L)).thenReturn(List.of(appointment));
        List<AppointmentResponseDTO> dtos = service.getAppointmentsByProvider(1L);
        assertEquals(1, dtos.size());
    }

    @Test
    void getAppointmentsByTenant() {
        Appointment appointment = new Appointment();
        appointment.setId(1L); 
        appointment.setProvider(new User());
        appointment.setCustomer(new User());
        appointment.setService(new ProvidedService());
        appointment.setTenant(new Tenant());
        appointment.setAppointmentTime(LocalDateTime.now());
        when(appointmentRepo.findByTenantId(1L)).thenReturn(List.of(appointment));
        List<AppointmentResponseDTO> dtos = service.getAppointmentsByTenant(1L);
        assertEquals(1, dtos.size());
    }
}