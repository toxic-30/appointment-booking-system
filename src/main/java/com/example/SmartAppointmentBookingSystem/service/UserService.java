package com.example.SmartAppointmentBookingSystem.service;

import java.util.*;
import com.example.SmartAppointmentBookingSystem.dto.user.UserRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.user.UserResponseDTO;

public interface UserService {

    List<UserResponseDTO> getAllUsers();
    UserResponseDTO addUser(UserRequestDTO userRequestDTO);
    void deleteUser(Long id);
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);
    Optional<UserResponseDTO> getUserById(Long id);
    Optional<UserResponseDTO> getUserByUserCode(String userCode);
}
 