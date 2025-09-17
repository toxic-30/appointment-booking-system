package com.example.SmartAppointmentBookingSystem.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.SmartAppointmentBookingSystem.entity.ProviderServiceMapping;

@Repository
public interface ProviderServiceMappingRepository extends JpaRepository <ProviderServiceMapping, Long>{
    
}
