package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.example.SmartAppointmentBookingSystem.dto.appointment.AppointmentResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.Appointment;
import com.example.SmartAppointmentBookingSystem.entity.ProvidedService;
import com.example.SmartAppointmentBookingSystem.entity.Tenant;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.enums.AppointmentStatus;
import com.example.SmartAppointmentBookingSystem.enums.UserRole;
import com.example.SmartAppointmentBookingSystem.exception.ResourceNotFoundException;
import com.example.SmartAppointmentBookingSystem.repository.AppointmentRepository;
import com.example.SmartAppointmentBookingSystem.repository.ProvidedServiceRepository;
import com.example.SmartAppointmentBookingSystem.repository.TenantRepository;
import com.example.SmartAppointmentBookingSystem.repository.UserRepository;
import com.example.SmartAppointmentBookingSystem.service.NotificationService;
import com.example.SmartAppointmentBookingSystem.util.ReminderUtil;

@ExtendWith(SpringExtension.class)
class AppointmentServiceImplTest {

    @Mock private AppointmentRepository appointmentRepo;
    @Mock private UserRepository userRepo;
    @Mock private TenantRepository tenantRepo;
    @Mock private ProvidedServiceRepository serviceRepo;
    @Mock private NotificationService notificationService;
    @Mock private ReminderUtil reminderUtil;

    @InjectMocks private AppointmentServiceImpl appointmentService;

    private User adminUser;
    private User providerUser;
    private User customerUser;
    private Appointment appointment;
    private Tenant tenant;
    private ProvidedService service;

    @BeforeEach
    void setUp() {
        // Users
        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setRole(UserRole.ADMIN);

        providerUser = new User();
        providerUser.setId(2L);
        providerUser.setRole(UserRole.PROVIDER);
        providerUser.setName("Provider One");

        customerUser = new User();
        customerUser.setId(3L);
        customerUser.setRole(UserRole.CUSTOMER);
        customerUser.setName("Customer One");

        // Tenant
        tenant = new Tenant();
        tenant.setId(10L);
        providerUser.setTenant(tenant);
        customerUser.setTenant(tenant);

        // Service
        service = new ProvidedService();
        service.setId(1000L);
        service.setName("Haircut");
        service.setDurationMinutes(60);

        // Appointment
        appointment = new Appointment();
        appointment.setId(100L);
        appointment.setProvider(providerUser);
        appointment.setCustomer(customerUser);
        appointment.setTenant(tenant);
        appointment.setService(service);

        LocalDateTime now = LocalDateTime.now();
        appointment.setAppointmentTime(now.plusDays(1));
        appointment.setCreatedAt(now);
        appointment.setUpdatedAt(now);

        appointment.setStatus(AppointmentStatus.PENDING);
    }

    @Test
    void testGetAppointmentsByTenant_asAdmin() {
        when(appointmentRepo.findByTenantId(tenant.getId())).thenReturn(List.of(appointment));

        List<AppointmentResponseDTO> result = appointmentService.getAppointmentsByTenant(tenant.getId(), adminUser);

        assertEquals(1, result.size());
        AppointmentResponseDTO dto = result.get(0);
        assertEquals(appointment.getId(), dto.getId());
        assertEquals(service.getName(), dto.getServiceName());
        assertEquals(providerUser.getName(), dto.getProviderName());
        assertEquals(appointment.getAppointmentTime(), dto.getAppointmentTime());
        assertEquals(appointment.getCreatedAt(), dto.getCreatedAt());
        assertEquals(appointment.getUpdatedAt(), dto.getUpdatedAt());
    }

    @Test
    void testGetAppointmentsByTenant_asProvider() {
        when(appointmentRepo.findByTenantId(tenant.getId())).thenReturn(List.of(appointment));

        List<AppointmentResponseDTO> result = appointmentService.getAppointmentsByTenant(tenant.getId(), providerUser);

        assertEquals(1, result.size());
        AppointmentResponseDTO dto = result.get(0);
        assertEquals(appointment.getId(), dto.getId());
        assertEquals(appointment.getAppointmentTime(), dto.getAppointmentTime());
    }

    @Test
    void testDeleteAppointment_softDeleteByProvider() {
        when(appointmentRepo.findById(appointment.getId())).thenReturn(Optional.of(appointment));

        assertDoesNotThrow(() -> appointmentService.deleteAppointment(appointment.getId(), providerUser));
        assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
        verify(appointmentRepo).save(appointment);
    }

    @Test
    void testDeleteAppointment_notAllowed() {
        User otherProvider = new User();
        otherProvider.setId(4L);
        otherProvider.setRole(UserRole.PROVIDER);
        otherProvider.setTenant(tenant);

        when(appointmentRepo.findById(appointment.getId())).thenReturn(Optional.of(appointment));

        assertThrows(AccessDeniedException.class,
                () -> appointmentService.deleteAppointment(appointment.getId(), otherProvider));
    }

    @Test
    void testGetAppointmentById_found() {
        when(appointmentRepo.findById(appointment.getId())).thenReturn(Optional.of(appointment));

        AppointmentResponseDTO dto = appointmentService.getAppointmentById(appointment.getId(), customerUser);

        assertEquals(appointment.getId(), dto.getId());
        assertEquals(service.getName(), dto.getServiceName());
        assertEquals(providerUser.getName(), dto.getProviderName());
        assertEquals(appointment.getAppointmentTime(), dto.getAppointmentTime());
        assertEquals(appointment.getCreatedAt(), dto.getCreatedAt());
        assertEquals(appointment.getUpdatedAt(), dto.getUpdatedAt());
    }

    @Test
    void testGetAppointmentById_notFound() {
        when(appointmentRepo.findById(appointment.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.getAppointmentById(appointment.getId(), adminUser));
    }

    @Test
    void testGetAppointmentsByProvider_asProvider() {
        when(appointmentRepo.findByProviderId(providerUser.getId())).thenReturn(List.of(appointment));

        List<AppointmentResponseDTO> result = appointmentService.getAppointmentsByProvider(providerUser.getId(), providerUser);

        assertEquals(1, result.size());
        AppointmentResponseDTO dto = result.get(0);
        assertEquals(appointment.getId(), dto.getId());
        assertEquals(appointment.getAppointmentTime(), dto.getAppointmentTime());
    }

    @Test
    void testGetAppointmentsByCustomer_asCustomer() {
        when(appointmentRepo.findByCustomerId(customerUser.getId())).thenReturn(List.of(appointment));

        List<AppointmentResponseDTO> result = appointmentService.getAppointmentsByCustomer(customerUser.getId(), customerUser);

        assertEquals(1, result.size());
        AppointmentResponseDTO dto = result.get(0);
        assertEquals(appointment.getId(), dto.getId());
        assertEquals(appointment.getAppointmentTime(), dto.getAppointmentTime());
    }
}
