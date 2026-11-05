package ru.example.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.example.userservice.dto.UserRequestDto;
import ru.example.userservice.dto.UserResponseDto;
import ru.example.userservice.exception.UserServiceException;
import ru.example.userservice.model.User;
import ru.example.userservice.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void createUser_Success_WhenAgeIsAdult() {
        UserRequestDto request = new UserRequestDto("Иван", "ivan@mail.com", 25);
        User mockedUser = new User("Иван", "ivan@mail.com", 25);

        when(userRepository.save(any(User.class))).thenReturn(mockedUser);

        UserResponseDto response = userService.createUser(request);

        assertNotNull(response);
        assertEquals("Иван", response.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_ThrowsException_WhenUserIsUnderage() {
        UserRequestDto underageUser = new UserRequestDto("Миша", "misha@mail.com", 16);

        UserServiceException exception = assertThrows(UserServiceException.class, () -> {
            userService.createUser(underageUser);
        });

        assertEquals("Регистрация доступна только с 18 лет.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserById_Success_WhenIdIsValid() {
        User expectedUser = new User("Анна", "anna@mail.com", 20);
        expectedUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));

        UserResponseDto actualUser = userService.getUserById(1L);

        assertNotNull(actualUser);
        assertEquals("Анна", actualUser.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_ThrowsException_WhenIdIsNegative() {
        UserServiceException exception = assertThrows(UserServiceException.class, () -> {
            userService.getUserById(-5L);
        });

        assertEquals("Некорректный ID пользователя.", exception.getMessage());
        verify(userRepository, never()).findById(anyLong());
    }
}
