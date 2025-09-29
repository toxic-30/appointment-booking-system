package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import com.example.SmartAppointmentBookingSystem.dto.tenant.TenantRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.tenant.TenantResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.Tenant;
import com.example.SmartAppointmentBookingSystem.exception.ResourceNotFoundException;
import com.example.SmartAppointmentBookingSystem.repository.TenantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TenantServiceImplementationTest {

    @Mock TenantRepository tenantRepo;
    @InjectMocks TenantServiceImplementation service;

    @Test
    void getAllTenants_returnsList() {
        Tenant tenant = new Tenant();
        tenant.setName("Tenant1");
        when(tenantRepo.findAll()).thenReturn(List.of(tenant));
        List<TenantResponseDTO> tenants = service.getAllTenants();
        assertEquals(1, tenants.size());
        assertEquals("Tenant1", tenants.get(0).getName());
    }

    @Test
    void getTenantById_found() {
        Tenant tenant = new Tenant();
        tenant.setName("Tenant1");
        when(tenantRepo.findById(1L)).thenReturn(Optional.of(tenant));
        TenantResponseDTO dto = service.getTenantById(1L);
        assertEquals("Tenant1", dto.getName());
    }

    @Test
    void getTenantById_notFound() {
        when(tenantRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getTenantById(1L));
    }

    @Test
    void addTenant_success() {
        TenantRequestDTO dto = new TenantRequestDTO();
        dto.setName("Tenant1");
        dto.setEmail("tenant@example.com");
        when(tenantRepo.findByEmail("tenant@example.com")).thenReturn(Optional.empty());
        when(tenantRepo.save(any())).thenAnswer(i -> {
            Tenant t = i.getArgument(0);
            t.setId(5L);
            return t;
        });
        TenantResponseDTO result = service.addTenant(dto);
        assertEquals("Tenant1", result.getName());
        assertEquals(5L, result.getId());
    }

    @Test
    void addTenant_duplicateEmail_throws() {
        TenantRequestDTO dto = new TenantRequestDTO();
        dto.setEmail("tenant@example.com");
        when(tenantRepo.findByEmail("tenant@example.com")).thenReturn(Optional.of(new Tenant()));
        assertThrows(com.example.SmartAppointmentBookingSystem.exception.DuplicateResourceException.class, () -> service.addTenant(dto));
    }

    @Test
    void updateTenant_found() {
        Tenant tenant = new Tenant();
        tenant.setName("Old");
        when(tenantRepo.findById(1L)).thenReturn(Optional.of(tenant));
        when(tenantRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        TenantRequestDTO dto = new TenantRequestDTO();
        dto.setName("New");
        dto.setEmail("new@example.com");
        dto.setContactNumber("12345");
        dto.setAddress("Addr");
        TenantResponseDTO result = service.updateTenant(1L, dto);
        assertEquals("New", result.getName());
        assertEquals("new@example.com", result.getEmail());
    }

    @Test
    void updateTenant_notFound() {
        when(tenantRepo.findById(1L)).thenReturn(Optional.empty());
        TenantRequestDTO dto = new TenantRequestDTO();
        assertThrows(ResourceNotFoundException.class, () -> service.updateTenant(1L, dto));
    }

    @Test
    void deleteTenant_found() {
        when(tenantRepo.existsById(1L)).thenReturn(true);
        doNothing().when(tenantRepo).deleteById(1L);
        assertDoesNotThrow(() -> service.deleteTenant(1L));
    }

    @Test
    void deleteTenant_notFound() {
        when(tenantRepo.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.deleteTenant(1L));
    }
}
