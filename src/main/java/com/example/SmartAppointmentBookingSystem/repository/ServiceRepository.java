package com.example.SmartAppointmentBookingSystem.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SmartAppointmentBookingSystem.entity.Service;

@Repository
public interface ServiceRepository extends JpaRepository <Service, Long>{
    
}
