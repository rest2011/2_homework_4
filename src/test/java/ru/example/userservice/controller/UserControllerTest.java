package ru.example.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.example.userservice.dto.UserRequestDto;
import ru.example.userservice.dto.UserResponseDto;
import ru.example.userservice.service.UserService;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void createUser_Success_WhenDataIsValid() throws Exception {
        UserRequestDto request = new UserRequestDto("Дмитрий", "dima@example.com", 25);
        UserResponseDto response = new UserResponseDto(1L, "Дмитрий", "dima@example.com", 25, LocalDateTime.now());

        when(userService.createUser(any(UserRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Дмитрий"))
                .andExpect(jsonPath("$.email").value("dima@example.com"));
    }

    @Test
    void createUser_BadRequest_WhenEmailIsInvalid() throws Exception {
        UserRequestDto request = new UserRequestDto("Дмитрий", "not-an-email", 25);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Введен некорректный формат Email"));
    }
}
