package com.example.SmartAppointmentBookingSystem.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.SmartAppointmentBookingSystem.entity.ProvidedService;

@Repository
public interface ProvidedServiceRepository extends JpaRepository <ProvidedService, Long>{
    List<ProvidedService> findByTenantId(Long tenantId);
}
