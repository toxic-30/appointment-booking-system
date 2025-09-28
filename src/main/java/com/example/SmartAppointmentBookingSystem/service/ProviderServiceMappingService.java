package com.example.SmartAppointmentBookingSystem.service;

import java.util.List;
import com.example.SmartAppointmentBookingSystem.entity.ProviderServiceMapping;

public interface ProviderServiceMappingService {

    ProviderServiceMapping createMapping(ProviderServiceMapping mapping);
    ProviderServiceMapping getMappingById(Long id);
    List<ProviderServiceMapping> getAllMappings();
    List<ProviderServiceMapping> getMappingsByProvider(Long providerId);
    List<ProviderServiceMapping> getMappingsByService(Long serviceId);
    List<ProviderServiceMapping> getMappingsByTenant(Long tenantId);
    ProviderServiceMapping updateMapping(Long id, ProviderServiceMapping updatedMapping);
    void deleteMapping(Long id);
}
