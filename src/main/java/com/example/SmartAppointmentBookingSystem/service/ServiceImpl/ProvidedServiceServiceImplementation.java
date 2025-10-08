package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.example.SmartAppointmentBookingSystem.dto.providedService.ProvidedServiceRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.providedService.ProvidedServiceResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.ProvidedService;
import com.example.SmartAppointmentBookingSystem.entity.Tenant;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.enums.UserRole;
import com.example.SmartAppointmentBookingSystem.exception.ResourceNotFoundException;
import com.example.SmartAppointmentBookingSystem.repository.ProvidedServiceRepository;
import com.example.SmartAppointmentBookingSystem.repository.TenantRepository;
import com.example.SmartAppointmentBookingSystem.service.ProvidedServiceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProvidedServiceServiceImplementation implements ProvidedServiceService {

    private final ProvidedServiceRepository providedServiceRepo;
    private final TenantRepository tenantRepo;

    @Override
    public List<ProvidedServiceResponseDTO> getAllServices() {
        List<ProvidedService> services = providedServiceRepo.findAll();
        return services.stream().map(this::toResponseDTO).toList();
    }

    @Override
    public List<ProvidedServiceResponseDTO> getServicesByTenant(Long tenantId) {
        List<ProvidedService> services = providedServiceRepo.findByTenantId(tenantId);
        return services.stream().map(this::toResponseDTO).toList();
    }

    @Override
    public ProvidedServiceResponseDTO getServiceById(Long id) {
        ProvidedService service = providedServiceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));
        return toResponseDTO(service);
    }

    @Override
    public ProvidedServiceResponseDTO addService(ProvidedServiceRequestDTO serviceRequestDTO,User currentUser) {
        Tenant tenant = tenantRepo.findById(serviceRequestDTO.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + serviceRequestDTO.getTenantId()));

        if (currentUser.getRole() == UserRole.PROVIDER) {
        if (!tenant.getUsers().contains(currentUser)) {
            throw new AccessDeniedException("Provider can only add services to their own tenant.");}
        } else if (currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Only admins or tenant owners can add services.");
    }        
        ProvidedService service = new ProvidedService();
        service.setName(serviceRequestDTO.getName());
        service.setDescription(serviceRequestDTO.getDescription());
        service.setPrice(serviceRequestDTO.getPrice());
        service.setDurationMinutes(serviceRequestDTO.getDurationMinutes());
        service.setTenant(tenant);

        ProvidedService savedService = providedServiceRepo.save(service);
        return toResponseDTO(savedService);
    }
    @Override
    public ProvidedServiceResponseDTO updateService(Long id, ProvidedServiceRequestDTO serviceRequestDTO,
            User currentUser) {
        ProvidedService service = providedServiceRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));
        Tenant tenant = service.getTenant();
        if (currentUser.getRole() == UserRole.PROVIDER) {
            if (!tenant.getUsers().contains(currentUser)) {
               throw new AccessDeniedException("Provider can only update services in their own tenant.");
            }
        } else if (currentUser.getRole() != UserRole.ADMIN) {
        throw new AccessDeniedException("Only admins or tenant owners can update services.");
        }
        service.setName(serviceRequestDTO.getName());
        service.setDescription(serviceRequestDTO.getDescription());
        service.setPrice(serviceRequestDTO.getPrice());
        service.setDurationMinutes(serviceRequestDTO.getDurationMinutes());
        ProvidedService updatedService = providedServiceRepo.save(service);
        return toResponseDTO(updatedService);
    }

    @Override
    public void deleteService(Long id, User currentUser) {
        ProvidedService service = providedServiceRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));
        Tenant tenant = service.getTenant();
        // Role & ownership check
        if (currentUser.getRole() == UserRole.PROVIDER) {
            if (!tenant.getUsers().contains(currentUser)) {
               throw new AccessDeniedException("Provider can only delete services in their own tenant."); }
        } else if (currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Only admins or tenant owners can delete services.");
        }
        providedServiceRepo.delete(service);
    }

    private ProvidedServiceResponseDTO toResponseDTO(ProvidedService service) {
        ProvidedServiceResponseDTO dto = new ProvidedServiceResponseDTO();
        dto.setId(service.getId());
        dto.setName(service.getName());
        dto.setDescription(service.getDescription());
        dto.setPrice(service.getPrice());
        dto.setDurationMinutes(service.getDurationMinutes());
        if (service.getTenant() != null) {
            dto.setTenantId(service.getTenant().getId());
            dto.setTenantName(service.getTenant().getName());
        }
        return dto;
    }


}
