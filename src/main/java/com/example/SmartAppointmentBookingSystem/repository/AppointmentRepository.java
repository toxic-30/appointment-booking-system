package com.example.SmartAppointmentBookingSystem.repository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.SmartAppointmentBookingSystem.entity.Appointment;
import com.example.SmartAppointmentBookingSystem.enums.AppointmentStatus;

@Repository
public interface AppointmentRepository extends JpaRepository <Appointment, Long>{

    List<Appointment> findByProviderId(Long providerId);
    List<Appointment> findByCustomerId(Long customerId);
    List<Appointment> findByTenantId(Long tenantId);
    List<Appointment> findByStatus(AppointmentStatus status);
    List<Appointment> findByAppointmentTimeBetween(LocalDateTime start, LocalDateTime end);
    
}
