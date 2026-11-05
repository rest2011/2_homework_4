package ru.example.userservice.service;

import ru.example.userservice.dto.UserRequestDto;
import ru.example.userservice.dto.UserResponseDto;
import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userDto);
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getAllUsers();
    UserResponseDto updateUser(Long id, UserRequestDto userDto);
    void deleteUser(Long id);
}
