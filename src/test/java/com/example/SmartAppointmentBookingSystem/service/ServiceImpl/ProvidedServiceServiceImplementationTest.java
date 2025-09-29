package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import com.example.SmartAppointmentBookingSystem.dto.providedService.ProvidedServiceRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.providedService.ProvidedServiceResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.ProvidedService;
import com.example.SmartAppointmentBookingSystem.entity.Tenant;
import com.example.SmartAppointmentBookingSystem.exception.ResourceNotFoundException;
import com.example.SmartAppointmentBookingSystem.repository.ProvidedServiceRepository;
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
class ProvidedServiceServiceImplementationTest {

    @Mock ProvidedServiceRepository providedServiceRepo;
    @Mock TenantRepository tenantRepo;
    @InjectMocks ProvidedServiceServiceImplementation service;

    @Test
    void getAllServices_returnsList() {
        ProvidedService serviceEntity = new ProvidedService();
        serviceEntity.setName("Service1");
        when(providedServiceRepo.findAll()).thenReturn(List.of(serviceEntity));
        List<ProvidedServiceResponseDTO> services = service.getAllServices();
        assertEquals(1, services.size());
        assertEquals("Service1", services.get(0).getName());
    }

    @Test
    void getServicesByTenant_returnsList() {
        ProvidedService serviceEntity = new ProvidedService();
        serviceEntity.setName("Service1");
        when(providedServiceRepo.findByTenantId(2L)).thenReturn(List.of(serviceEntity));
        List<ProvidedServiceResponseDTO> services = service.getServicesByTenant(2L);
        assertEquals(1, services.size());
    }

    @Test
    void getServiceById_found() {
        ProvidedService serviceEntity = new ProvidedService();
        serviceEntity.setName("Service1");
        when(providedServiceRepo.findById(1L)).thenReturn(Optional.of(serviceEntity));
        ProvidedServiceResponseDTO dto = service.getServiceById(1L);
        assertEquals("Service1", dto.getName());
    }

    @Test
    void getServiceById_notFound() {
        when(providedServiceRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getServiceById(1L));
    }

    @Test
    void addService_success() {
        ProvidedServiceRequestDTO dto = new ProvidedServiceRequestDTO();
        dto.setName("Service1");
        dto.setDescription("Desc");
        dto.setPrice(100.0);
        dto.setDurationMinutes(30);
        dto.setTenantId(2L);

        Tenant tenant = new Tenant();
        tenant.setId(2L);
        tenant.setName("Tenant1");

        when(tenantRepo.findById(2L)).thenReturn(Optional.of(tenant));
        when(providedServiceRepo.save(any())).thenAnswer(i -> {
            ProvidedService s = i.getArgument(0);
            s.setId(1L);
            return s;
        });

        ProvidedServiceResponseDTO result = service.addService(dto);
        assertEquals("Service1", result.getName());
        assertEquals("Tenant1", result.getTenantName());
    }

    @Test
    void addService_tenantNotFound_throws() {
        ProvidedServiceRequestDTO dto = new ProvidedServiceRequestDTO();
        dto.setTenantId(2L);
        when(tenantRepo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.addService(dto));
    }

    @Test
    void updateService_found() {
        ProvidedService serviceEntity = new ProvidedService();
        serviceEntity.setId(1L);
        serviceEntity.setName("Old");
        when(providedServiceRepo.findById(1L)).thenReturn(Optional.of(serviceEntity));
        when(providedServiceRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        ProvidedServiceRequestDTO dto = new ProvidedServiceRequestDTO();
        dto.setName("New");
        dto.setDescription("Desc");
        dto.setPrice(200.0);
        dto.setDurationMinutes(45);
        ProvidedServiceResponseDTO result = service.updateService(1L, dto);
        assertEquals("New", result.getName());
    }

    @Test
    void updateService_notFound() {
        ProvidedServiceRequestDTO dto = new ProvidedServiceRequestDTO();
        when(providedServiceRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.updateService(1L, dto));
    }

    @Test
    void deleteService_found() {
        ProvidedService serviceEntity = new ProvidedService();
        serviceEntity.setId(1L);
        when(providedServiceRepo.findById(1L)).thenReturn(Optional.of(serviceEntity));
        doNothing().when(providedServiceRepo).delete(serviceEntity);
        assertDoesNotThrow(() -> service.deleteService(1L));
    }

    @Test
    void deleteService_notFound() {
        when(providedServiceRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.deleteService(1L));
    }
}
