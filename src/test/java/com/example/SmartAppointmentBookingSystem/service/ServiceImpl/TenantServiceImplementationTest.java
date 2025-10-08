package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import com.example.SmartAppointmentBookingSystem.dto.tenant.TenantRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.tenant.TenantResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.Tenant;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.enums.UserRole;
import com.example.SmartAppointmentBookingSystem.exception.ResourceNotFoundException;
import com.example.SmartAppointmentBookingSystem.repository.TenantRepository;
import com.example.SmartAppointmentBookingSystem.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TenantServiceImplementationTest {

    @Mock private TenantRepository tenantRepo;
    @Mock private UserRepository userRepo;

    @InjectMocks private TenantServiceImplementation tenantService;

    private User adminUser;
    private User providerUser;
    private Tenant tenant;
    private TenantRequestDTO tenantRequestDTO;

    @BeforeEach
    void setUp() {
        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setRole(UserRole.ADMIN);

        providerUser = new User();
        providerUser.setId(2L);
        providerUser.setRole(UserRole.PROVIDER);

        tenant = new Tenant();
        tenant.setId(1L);
        tenant.setName("Test Tenant");

        providerUser.setTenant(tenant);

        tenantRequestDTO = new TenantRequestDTO();
        tenantRequestDTO.setName("New Tenant");
        tenantRequestDTO.setAddress("123 Street");
        tenantRequestDTO.setEmail("tenant@example.com");
        tenantRequestDTO.setContactNumber("1234567890");
    }

    @Test
    void testGetAllTenants() {
        when(tenantRepo.findAll()).thenReturn(List.of(tenant));
        List<TenantResponseDTO> tenants = tenantService.getAllTenants();
        assertEquals(1, tenants.size());
        assertEquals(tenant.getName(), tenants.get(0).getName());
    }

    @Test
    void testGetTenantById_Found() {
        when(tenantRepo.findById(1L)).thenReturn(Optional.of(tenant));
        TenantResponseDTO dto = tenantService.getTenantById(1L);
        assertEquals(tenant.getName(), dto.getName());
    }

    @Test
    void testGetTenantById_NotFound() {
        when(tenantRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> tenantService.getTenantById(1L));
    }

    @Test
    void testAddTenant_AsAdmin() {
        when(tenantRepo.save(any(Tenant.class))).thenReturn(tenant);
        TenantResponseDTO dto = tenantService.addTenant(tenantRequestDTO, adminUser);
        assertEquals(tenant.getName(), dto.getName());
    }

    @Test
    void testAddTenant_AsProvider_FirstTime() {
        when(tenantRepo.existsByUsers_Id(providerUser.getId())).thenReturn(false);
        when(tenantRepo.save(any(Tenant.class))).thenReturn(tenant);
        when(userRepo.save(any(User.class))).thenReturn(providerUser);

        TenantResponseDTO dto = tenantService.addTenant(tenantRequestDTO, providerUser);
        assertEquals(tenant.getName(), dto.getName());
        assertEquals(tenant, providerUser.getTenant());
    }

    @Test
    void testAddTenant_AsProvider_AlreadyHasTenant() {
        when(tenantRepo.existsByUsers_Id(providerUser.getId())).thenReturn(true);
        assertThrows(IllegalStateException.class,
                () -> tenantService.addTenant(tenantRequestDTO, providerUser));
    }

    @Test
    void testAddTenant_AsCustomer_NotAllowed() {
        User customer = new User();
        customer.setId(3L);
        customer.setRole(UserRole.CUSTOMER);
        assertThrows(AccessDeniedException.class,
                () -> tenantService.addTenant(tenantRequestDTO, customer));
    }

    @Test
    void testUpdateTenant_AsAdmin() {
        when(tenantRepo.findById(1L)).thenReturn(Optional.of(tenant));
        when(tenantRepo.save(any(Tenant.class))).thenReturn(tenant);
        TenantResponseDTO dto = tenantService.updateTenant(1L, tenantRequestDTO, adminUser);
        assertEquals(tenant.getName(), dto.getName());
    }

    @Test
    void testUpdateTenant_AsProvider_Owner() {
        when(tenantRepo.findById(1L)).thenReturn(Optional.of(tenant));
        when(tenantRepo.save(any(Tenant.class))).thenReturn(tenant);
        TenantResponseDTO dto = tenantService.updateTenant(1L, tenantRequestDTO, providerUser);
        assertEquals(tenant.getName(), dto.getName());
    }

    @Test
    void testUpdateTenant_AsProvider_NotOwner() {
        Tenant otherTenant = new Tenant();
        otherTenant.setId(2L);
        User provider = new User();
        provider.setId(4L);
        provider.setRole(UserRole.PROVIDER);
        provider.setTenant(otherTenant);

        when(tenantRepo.findById(1L)).thenReturn(Optional.of(tenant));

        assertThrows(AccessDeniedException.class,
                () -> tenantService.updateTenant(1L, tenantRequestDTO, provider));
    }

    @Test
    void testDeleteTenant_AsAdmin() {
        when(tenantRepo.findById(1L)).thenReturn(Optional.of(tenant));
        assertDoesNotThrow(() -> tenantService.deleteTenant(1L, adminUser));
        verify(tenantRepo, times(1)).delete(tenant);
    }

    @Test
    void testDeleteTenant_AsProvider_Owner() {
        when(tenantRepo.findById(1L)).thenReturn(Optional.of(tenant));
        assertDoesNotThrow(() -> tenantService.deleteTenant(1L, providerUser));
        verify(tenantRepo, times(1)).delete(tenant);
    }

    @Test
    void testDeleteTenant_AsProvider_NotOwner() {
        Tenant otherTenant = new Tenant();
        otherTenant.setId(2L);
        User provider = new User();
        provider.setId(4L);
        provider.setRole(UserRole.PROVIDER);
        provider.setTenant(otherTenant);

        when(tenantRepo.findById(1L)).thenReturn(Optional.of(tenant));

        assertThrows(AccessDeniedException.class,
                () -> tenantService.deleteTenant(1L, provider));
    }
}
