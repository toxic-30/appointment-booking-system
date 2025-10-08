package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;

import com.example.SmartAppointmentBookingSystem.dto.user.UserRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.user.UserResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.Tenant;
import com.example.SmartAppointmentBookingSystem.entity.User;
import com.example.SmartAppointmentBookingSystem.enums.UserRole;
import com.example.SmartAppointmentBookingSystem.exception.ResourceNotFoundException;
import com.example.SmartAppointmentBookingSystem.repository.TenantRepository;
import com.example.SmartAppointmentBookingSystem.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceImplementationTest {

    @Mock UserRepository userRepo;
    @Mock TenantRepository tenantRepo;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks UserServiceImplementation service;

    private User currentUser;

    @BeforeEach
    void setup() {
        // Setup current user as admin for role-based tests
        currentUser = new User();
        currentUser.setId(999L);
        currentUser.setRole(UserRole.ADMIN);
        currentUser.setTenant(null); // admin may not have tenant
    }

    @Test
    void getAllUsers_returnsList() {
        User user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setEmail("test@example.com");
        user.setRole(UserRole.ADMIN);
        when(userRepo.findAll()).thenReturn(List.of(user));

        List<UserResponseDTO> users = service.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("Test", users.get(0).getName());
    }

    @Test
    void addUser_success_admin() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setName("AdminUser");
        dto.setEmail("admin@example.com");
        dto.setPassword("pass");
        dto.setRole(UserRole.ADMIN);

        when(userRepo.findByEmail("admin@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("encoded");
        when(userRepo.save(any())).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(1L);
            return u;
        });

        UserResponseDTO result = service.addUser(dto, currentUser);
        assertEquals("AdminUser", result.getName());
        assertEquals(UserRole.ADMIN, result.getRole());
    }

    @Test
    void addUser_providerWithTenant_createsTenant() {
        currentUser.setRole(UserRole.ADMIN);
        UserRequestDTO dto = new UserRequestDTO();
        dto.setName("Provider");
        dto.setEmail("provider@example.com");
        dto.setPassword("pass");
        dto.setRole(UserRole.PROVIDER);
        dto.setTenantName("TenantName");
        dto.setTenantEmail("tenant@example.com");
        dto.setTenantAddress("Addr");
        dto.setTenantContactNumber("12345");

        when(userRepo.findByEmail("provider@example.com")).thenReturn(Optional.empty());
        when(tenantRepo.findByEmail("tenant@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("encoded");

        when(tenantRepo.save(any())).thenAnswer(i -> {
            Tenant t = i.getArgument(0);
            t.setId(2L);
            return t;
        });

        when(userRepo.save(any())).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(1L);
            return u;
        });

        UserResponseDTO result = service.addUser(dto, currentUser);
        assertEquals("Provider", result.getName());
        assertEquals("TenantName", result.getTenantName());
    }

    @Test
    void addUser_duplicateEmail_throws() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("test@example.com");

        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(new User()));

        assertThrows(com.example.SmartAppointmentBookingSystem.exception.DuplicateResourceException.class,
                () -> service.addUser(dto, currentUser));
    }

    @Test
    void deleteUser_found() {
        User user = new User();
        user.setId(1L);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepo).delete(user);

        assertDoesNotThrow(() -> service.deleteUser(1L));
    }

    @Test
    void deleteUser_notFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.deleteUser(1L));
    }

    @Test
    void updateUser_found() {
        User user = new User();
        user.setId(1L);
        user.setName("Old");
        user.setEmail("old@example.com");

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        UserRequestDTO dto = new UserRequestDTO();
        dto.setName("New");
        dto.setEmail("new@example.com");
        dto.setPassword("newpass");

        UserResponseDTO result = service.updateUser(1L, dto);
        assertEquals("New", result.getName());
        assertEquals("new@example.com", result.getEmail());
    }

    @Test
    void getUserById_found() {
        User user = new User();
        user.setId(1L);
        user.setName("Test");

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserResponseDTO> result = service.getUserById(1L);
        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getName());
    }

    @Test
    void getUserByUserCode_found() {
        User user = new User();
        user.setUserCode("CODE123");
        user.setName("Test");

        when(userRepo.findByUserCode("CODE123")).thenReturn(Optional.of(user));

        Optional<UserResponseDTO> result = service.getUserByUserCode("CODE123");
        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getName());
    }
}
