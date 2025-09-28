package com.example.SmartAppointmentBookingSystem.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.SmartAppointmentBookingSystem.entity.ProviderServiceMapping;

@Repository
public interface ProviderServiceMappingRepository extends JpaRepository <ProviderServiceMapping, Long>{

    List<ProviderServiceMapping> findByProviderId(Long providerId);
    List<ProviderServiceMapping> findByServiceId(Long serviceId);
    List<ProviderServiceMapping> findByTenantId(Long tenantId);    
}
