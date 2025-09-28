package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.repository.UserRepository;
import com.example.SmartAppointmentBookingSystem.service.UserDetailsService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword()) // encoded
                .roles(user.getRole().name()) // ADMIN, PROVIDER, CUSTOMER
                .build();
    }
}
