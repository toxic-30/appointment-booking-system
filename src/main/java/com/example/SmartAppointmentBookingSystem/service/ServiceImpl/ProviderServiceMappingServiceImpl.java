package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.example.SmartAppointmentBookingSystem.entity.ProvidedService;
import com.example.SmartAppointmentBookingSystem.entity.ProviderServiceMapping;
import com.example.SmartAppointmentBookingSystem.entity.Tenant;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.exception.ResourceNotFoundException;
import com.example.SmartAppointmentBookingSystem.repository.ProvidedServiceRepository;
import com.example.SmartAppointmentBookingSystem.repository.ProviderServiceMappingRepository;
import com.example.SmartAppointmentBookingSystem.repository.TenantRepository;
import com.example.SmartAppointmentBookingSystem.repository.UserRepository;
import com.example.SmartAppointmentBookingSystem.service.ProviderServiceMappingService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProviderServiceMappingServiceImpl implements ProviderServiceMappingService {

    private final ProviderServiceMappingRepository repository;
    private final ProvidedServiceRepository serviceRepo;
    private final UserRepository userRepo;
    private final TenantRepository tenantRepo;

    // Delete mapping (Admin only)
    @Override
    public void deleteMapping(Long id,User currentUser) {
        if (!currentUser.getRole().equals("ADMIN")) {
            throw new AccessDeniedException("Only admin can delete mappings");
        }
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Mapping not found with id: " + id);
        }
        repository.deleteById(id);
    }


    // Create mapping
    @Override
    public ProviderServiceMapping createMapping(ProviderServiceMapping mapping, User currentUser) {
        User provider = userRepo.findById(mapping.getProvider().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        ProvidedService service = serviceRepo.findById(mapping.getService().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        Tenant tenant = tenantRepo.findById(mapping.getTenant().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));

        // Role-based enforcement
        if (currentUser.getRole().equals("PROVIDER")) {
            if (!provider.getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("Providers can only create mappings for themselves");
            }
            mapping.setProvider(currentUser);
            mapping.setTenant(currentUser.getTenant());
            mapping.setIsActive(true);
        }

        mapping.setProvider(provider);
        mapping.setService(service);
        mapping.setTenant(tenant);

        return repository.save(mapping);
    }    

    @Override
    public ProviderServiceMapping getMappingById(Long id, User currentUser) {
        ProviderServiceMapping mapping = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mapping not found with id: " + id));

        if (currentUser.getRole().equals("PROVIDER") &&
            !mapping.getProvider().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Providers can only access their own mappings");
        }
        return mapping;
    }

    @Override
    public List<ProviderServiceMapping> getAllMappings(User currentUser) {
        if (currentUser.getRole().equals("ADMIN")) {
            return repository.findAll();
        } else if (currentUser.getRole().equals("PROVIDER")) {
            return repository.findByProviderId(currentUser.getId());
        } else {
            throw new AccessDeniedException("Only admin or provider can view mappings");
        }
    }

    @Override
    public ProviderServiceMapping updateMapping(Long id, ProviderServiceMapping updatedMapping, User currentUser) {
               ProviderServiceMapping existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mapping not found with id: " + id));

        // Role enforcement
        if (currentUser.getRole().equals("PROVIDER") &&
            !existing.getProvider().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Providers can only update their own mappings");
        }

        // Update allowed fields
        existing.setPriceOverride(updatedMapping.getPriceOverride());
        existing.setDurationOverrideMinutes(updatedMapping.getDurationOverrideMinutes());
        existing.setIsActive(updatedMapping.getIsActive());

        // Only admin can change provider/service/tenant
        if (currentUser.getRole().equals("ADMIN")) {
            User provider = userRepo.findById(updatedMapping.getProvider().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
            ProvidedService service = serviceRepo.findById(updatedMapping.getService().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
            Tenant tenant = tenantRepo.findById(updatedMapping.getTenant().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));

            existing.setProvider(provider);
            existing.setService(service);
            existing.setTenant(tenant);
        }
        return repository.save(existing);
    }
}
