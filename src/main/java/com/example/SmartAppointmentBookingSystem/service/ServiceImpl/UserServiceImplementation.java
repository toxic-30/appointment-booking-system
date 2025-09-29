package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.SmartAppointmentBookingSystem.dto.user.UserRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.user.UserResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.Tenant;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.enums.UserRole;
import com.example.SmartAppointmentBookingSystem.exception.ResourceNotFoundException;
import com.example.SmartAppointmentBookingSystem.repository.TenantRepository;
import com.example.SmartAppointmentBookingSystem.repository.UserRepository;
import com.example.SmartAppointmentBookingSystem.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService{

    private final UserRepository userRepo;
    private final TenantRepository tenantRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO addUser(UserRequestDTO userRequestDTO) {

        if (userRepo.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            throw new com.example.SmartAppointmentBookingSystem.exception.DuplicateResourceException("User with email already exists");
        }

        User user = toEntity(userRequestDTO);
        if(user.getRole()== UserRole.PROVIDER){
            Tenant tenant ;

            if (userRequestDTO.getTenantId() != null) {
                tenant = tenantRepo.findById(userRequestDTO.getTenantId())
                        .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + userRequestDTO.getTenantId()));
            } else if (userRequestDTO.getTenantName() != null && !userRequestDTO.getTenantName().isBlank()) {
                // Check for duplicate tenant
                if (userRequestDTO.getTenantEmail() != null && tenantRepo.findByEmail(userRequestDTO.getTenantEmail()).isPresent()) {
                    throw new com.example.SmartAppointmentBookingSystem.exception.DuplicateResourceException("Tenant with email already exists");
                }
                tenant = Tenant.builder()
                        .name(userRequestDTO.getTenantName())
                        .address(userRequestDTO.getTenantAddress())
                        .contactNumber(userRequestDTO.getTenantContactNumber())
                        .email(userRequestDTO.getTenantEmail())
                        .build();
                tenantRepo.save(tenant);
            } else {
                throw new ResourceNotFoundException("Tenant information is required for PROVIDER role");
            }
            user.setTenant(tenant);
        }

        user.setUserCode(generateUserCode(user.getRole()));
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword())); // Secure password

        User savedUser = userRepo.save(user);
        return toResponseDTO(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepo.delete(user);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
         User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword())); // only update if provided
        }
        User updatedUser = userRepo.save(user);
        return toResponseDTO(updatedUser);
    }

    @Override
    public Optional<UserResponseDTO> getUserById(Long id) {
        return userRepo.findById(id).map(this::toResponseDTO);
    }
    @Override
    public Optional<UserResponseDTO> getUserByUserCode(String userCode) {
       return userRepo.findByUserCode(userCode).map(this::toResponseDTO);
    }

    private UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserCode(user.getUserCode());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        if (user.getTenant() != null) { // null safe
            dto.setTenantId(user.getTenant().getId());
            dto.setTenantName(user.getTenant().getName());
        }
        return dto;
    }
    private String generateUserCode(UserRole role) {
        String prefix;
        switch (role) {
            case ADMIN: prefix = "ADM"; break;
            case CUSTOMER: prefix = "CUS"; break;
            case PROVIDER: prefix = "PRO"; break;
            default: prefix = "USR";
        }
        // Safer user code (based on current timestamp, avoids collisions)
        long timestamp = System.currentTimeMillis() % 1000000;
        return prefix + String.format("%06d", timestamp);
    }

    private User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // raw for now, will be encoded later
        user.setRole(dto.getRole());
        return user;
    }
}
