package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.example.SmartAppointmentBookingSystem.dto.tenant.TenantRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.tenant.TenantResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.Tenant;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.enums.UserRole;
import com.example.SmartAppointmentBookingSystem.exception.ResourceNotFoundException;
import com.example.SmartAppointmentBookingSystem.repository.TenantRepository;
import com.example.SmartAppointmentBookingSystem.repository.UserRepository;
import com.example.SmartAppointmentBookingSystem.service.TenantService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TenantServiceImplementation implements TenantService{

    private final TenantRepository tenantRepo;
    private final UserRepository userRepo;

    @Override
    @Transactional(readOnly = true)
    public List<TenantResponseDTO> getAllTenants() {
        return tenantRepo.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
     @Transactional(readOnly = true)
    public TenantResponseDTO getTenantById(Long id) {
        Tenant tenant = tenantRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + id));
        return toResponseDTO(tenant);
    }
    @Override
    public TenantResponseDTO addTenant(TenantRequestDTO tenantRequestDTO, User currentUser) {
        if (currentUser.getRole() == UserRole.ADMIN) {
            // Admin can create any tenant
            Tenant tenant = toEntity(tenantRequestDTO);
            Tenant savedTenant = tenantRepo.save(tenant);
            return toResponseDTO(savedTenant);

        } else if (currentUser.getRole() == UserRole.PROVIDER) {
            // Provider can create only ONE tenant (their own)
            if (tenantRepo.existsByUsers_Id(currentUser.getId())) {
                throw new IllegalStateException("Provider already has a tenant.");
            }
            Tenant tenant = toEntity(tenantRequestDTO);
            tenant.getUsers().add(currentUser); // assign provider as owner
            Tenant savedTenant = tenantRepo.save(tenant);
            currentUser.setTenant(savedTenant);
            userRepo.save(currentUser);
            return toResponseDTO(savedTenant);

        } else {
            throw new AccessDeniedException("Only admins and providers can create tenants.");
        }
    }

    @Override
    public TenantResponseDTO updateTenant(Long id, TenantRequestDTO tenantRequestDTO, User currentUser) {
        Tenant tenant = tenantRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + id));
        if (currentUser.getRole() == UserRole.ADMIN ||
                (currentUser.getRole() == UserRole.PROVIDER && tenant.equals(currentUser.getTenant()))) {
            // allowed: admin, or the provider who owns this tenant
            tenant.setName(tenantRequestDTO.getName());
            tenant.setAddress(tenantRequestDTO.getAddress());
            tenant.setContactNumber(tenantRequestDTO.getContactNumber());
            Tenant updated = tenantRepo.save(tenant);
            return toResponseDTO(updated);
        }
        throw new AccessDeniedException("You are not allowed to update this tenant.");
    }

    @Override
    public void deleteTenant(Long id, User currentUser) {
        Tenant tenant = tenantRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + id));
        if (currentUser.getRole() == UserRole.ADMIN ||
                (currentUser.getRole() == UserRole.PROVIDER && tenant.equals(currentUser.getTenant()))) {
            tenantRepo.delete(tenant);
        } else {
            throw new AccessDeniedException("You are not allowed to delete this tenant.");
        }
    }

    private TenantResponseDTO toResponseDTO(Tenant tenant) {
        TenantResponseDTO dto = new TenantResponseDTO();
        dto.setId(tenant.getId());
        dto.setName(tenant.getName());
        dto.setEmail(tenant.getEmail());
        dto.setAddress(tenant.getAddress());
        dto.setContactNumber(tenant.getContactNumber());
        return dto;
    }
    private Tenant toEntity(TenantRequestDTO dto) {
        Tenant tenant = new Tenant();
        tenant.setName(dto.getName());
        tenant.setAddress(dto.getAddress());
        tenant.setContactNumber(dto.getContactNumber());
        tenant.setEmail(dto.getEmail());
        return tenant;
    }

}
