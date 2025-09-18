package com.example.SmartAppointmentBookingSystem.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.SmartAppointmentBookingSystem.entity.Notification;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.enums.NotificationStatus;

@Repository
public interface NotificationRepository extends JpaRepository <Notification, Long>{

    List<Notification> findByRecipient(User recipient);
    List<Notification> findByStatus(NotificationStatus status);
    
}
