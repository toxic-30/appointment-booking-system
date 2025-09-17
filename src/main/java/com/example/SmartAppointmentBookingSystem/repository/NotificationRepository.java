package com.example.SmartAppointmentBookingSystem.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.SmartAppointmentBookingSystem.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository <Notification, Long>{
    
}
