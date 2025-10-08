package com.example.SmartAppointmentBookingSystem.service;

import java.util.List;
import com.example.SmartAppointmentBookingSystem.entity.ProviderServiceMapping;
import com.example.SmartAppointmentBookingSystem.entity.User;

public interface ProviderServiceMappingService {

    ProviderServiceMapping createMapping(ProviderServiceMapping mapping,User currentUser);
    ProviderServiceMapping getMappingById(Long id,User currentUser);
    List<ProviderServiceMapping> getAllMappings(User currentUser);
    ProviderServiceMapping updateMapping(Long id, ProviderServiceMapping updatedMapping,User currentUser);
    void deleteMapping(Long id,User currentUser);
}
