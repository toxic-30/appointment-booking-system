package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import java.util.List;
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

    @Override
    public ProviderServiceMapping getMappingById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mapping not found with id: " + id));
    }

    @Override
    public List<ProviderServiceMapping> getAllMappings() {
        return repository.findAll();
    }

    @Override
    public List<ProviderServiceMapping> getMappingsByProvider(Long providerId) {
        return repository.findByProviderId(providerId);
    }

    @Override
    public List<ProviderServiceMapping> getMappingsByService(Long serviceId) {
        return repository.findByServiceId(serviceId);
    }

    @Override
    public List<ProviderServiceMapping> getMappingsByTenant(Long tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Override
    public void deleteMapping(Long id) {
         if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Mapping not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public ProviderServiceMapping createMapping(ProviderServiceMapping mapping) {
        User provider = userRepo.findById(mapping.getProvider().getId())
        .orElseThrow(() -> new RuntimeException("Provider not found"));
        ProvidedService service = serviceRepo.findById(mapping.getService().getId())
        .orElseThrow(() -> new RuntimeException("Service not found"));
        Tenant tenant = tenantRepo.findById(mapping.getTenant().getId())
        .orElseThrow(() -> new RuntimeException("Tenant not found"));
        mapping.setProvider(provider);
        mapping.setService(service);
        mapping.setTenant(tenant);
        return repository.save(mapping);
    }

    @Override
    public ProviderServiceMapping updateMapping(Long id, ProviderServiceMapping updatedMapping) {
        return repository.findById(id).map(existing -> {
            existing.setPriceOverride(updatedMapping.getPriceOverride());
            existing.setDurationOverrideMinutes(updatedMapping.getDurationOverrideMinutes());
            existing.setIsActive(updatedMapping.getIsActive());
            existing.setService(updatedMapping.getService());
            existing.setProvider(updatedMapping.getProvider());
            existing.setTenant(updatedMapping.getTenant());
            return repository.save(existing);
        }).orElseThrow(() -> new ResourceNotFoundException("Mapping not found with id " + id));
}
}