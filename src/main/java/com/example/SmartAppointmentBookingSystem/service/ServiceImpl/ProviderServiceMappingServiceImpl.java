package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.SmartAppointmentBookingSystem.entity.ProviderServiceMapping;
import com.example.SmartAppointmentBookingSystem.exception.ResourceNotFoundException;
import com.example.SmartAppointmentBookingSystem.repository.ProviderServiceMappingRepository;
import com.example.SmartAppointmentBookingSystem.service.ProviderServiceMappingService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProviderServiceMappingServiceImpl implements ProviderServiceMappingService {

    private final ProviderServiceMappingRepository repository;

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