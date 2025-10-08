package com.example.SmartAppointmentBookingSystem.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository <User, Long>{

    Optional<User> findByUserCode(String userCode);
    Optional<User> findByEmail(String email);
    boolean existsByRole(UserRole role);
}
