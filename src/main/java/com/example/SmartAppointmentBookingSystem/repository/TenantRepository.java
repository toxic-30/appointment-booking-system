package com.example.SmartAppointmentBookingSystem.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.SmartAppointmentBookingSystem.entity.Tenant;

@Repository
public interface TenantRepository extends JpaRepository <Tenant, Long>{
    
}
