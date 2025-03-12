package com.example.demo.controllertest;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private com.example.demo.service.UserService userService;

    @InjectMocks
    private UserControllerTest userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        // Mock input data
        com.example.demo.dto.UserRequestDto requestDto = new com.example.demo.dto.UserRequestDto(
                "John Doe", "john.doe@example.com", 25, "France", "1234567890");

        com.example.demo.dto.UserResponseDto responseDto = new com.example.demo.dto.UserResponseDto(
                "12345", "John Doe", "john.doe@example.com", 25, "France", "1234567890");

        // Mock service method
        when(userService.registerUser(any(com.example.demo.dto.UserRequestDto.class))).thenReturn(responseDto);

        // Perform POST request
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("12345"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.age").value(25))
                .andExpect(jsonPath("$.country").value("France"))
                .andExpect(jsonPath("$.phoneNumber").value("1234567890"));

        // Verify service method call
        verify(userService, times(1)).registerUser(any(com.example.demo.dto.UserRequestDto.class));
    }
    @Test
    void testRegisterUser_InvalidAge() throws Exception {
        // Mock invalid user data (age < 18)
        com.example.demo.dto.UserRequestDto invalidRequest = new com.example.demo.dto.UserRequestDto(
                "Jane Doe", "jane.doe@example.com", 16, "France", "9876543210");

        // Perform POST request and expect bad request status
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUser_InvalidCountry() throws Exception {
        // Mock invalid user data (country not France)
        com.example.demo.dto.UserRequestDto invalidRequest = new com.example.demo.dto.UserRequestDto(
                "Jane Doe", "jane.doe@example.com", 25, "USA", "9876543210");

        // Mock service method to throw exception
        when(userService.registerUser(any(com.example.demo.dto.UserRequestDto.class)))
                .thenThrow(new IllegalArgumentException("Only users from France are allowed"));

        // Perform POST request and expect bad request status
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Only users from France are allowed"));
    }

    @Test
    void testGetUserById_Success() throws Exception {
        // Mock Data
        String userId = "12345";
        com.example.demo.dto.UserResponseDto mockUser = new com.example.demo.dto.UserResponseDto(
                userId, "John Doe", "john.doe@example.com", 25, "France", "1234567890");

        // Mock Service Call
        when(userService.getUserById(userId)).thenReturn(mockUser);

        // Perform GET Request and Validate Response
        mockMvc.perform(get("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.age").value(25))
                .andExpect(jsonPath("$.country").value("France"))
                .andExpect(jsonPath("$.phoneNumber").value("1234567890"));

        // Verify service method was called
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        // Mock Service Call
        String userId = "99999";
        when(userService.getUserById(userId)).thenThrow(new RuntimeException("User not found"));

        // Perform GET Request and Validate Response
        mockMvc.perform(get("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        // Verify service method was called
        verify(userService, times(1)).getUserById(userId);
    }
}
