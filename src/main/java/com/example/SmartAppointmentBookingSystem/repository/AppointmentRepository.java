package com.example.SmartAppointmentBookingSystem.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.SmartAppointmentBookingSystem.entity.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository <Appointment, Long>{
    
}
