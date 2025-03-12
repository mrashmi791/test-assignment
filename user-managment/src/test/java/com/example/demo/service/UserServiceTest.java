package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

public class UserServiceTest {

	 @Mock
	    private UserRepository userRepository;

	    @InjectMocks
	    private UserService userService;

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    void testRegisterUser_Success() {
	        // Mock request data
	        UserRequestDto requestDto = new UserRequestDto(
	                "John Doe", "john.doe@example.com", 25, "France", "1234567890");

	        // Mock saved user
	        User savedUser = User.builder()
	                .id("12345")
	                .name("John Doe")
	                .email("john.doe@example.com")
	                .age(25)
	                .country("France")
	                .phoneNumber("1234567890")
	                .build();

	        // Mock repository behavior
	        when(userRepository.save(any(User.class))).thenReturn(savedUser);

	        // Call service method
	        UserResponseDto responseDto = userService.registerUser(requestDto);

	        // Assertions
	        assertNotNull(responseDto);
	        assertEquals("12345", responseDto.getId());
	        assertEquals("John Doe", responseDto.getName());
	        assertEquals("john.doe@example.com", responseDto.getEmail());
	        assertEquals(25, responseDto.getAge());
	        assertEquals("France", responseDto.getCountry());
	        assertEquals("1234567890", responseDto.getPhoneNumber());

	        // Verify repository save was called
	        verify(userRepository, times(1)).save(any(User.class));
	    }

	    @Test
	    void testRegisterUser_InvalidCountry() {
	        // Mock invalid user request (not from France)
	        UserRequestDto invalidRequest = new UserRequestDto(
	                "Jane Doe", "jane.doe@example.com", 30, "USA", "9876543210");

	        // Expect IllegalArgumentException
	        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
	            userService.registerUser(invalidRequest);
	        });

	        // Verify the message
	        assertEquals("Only users from France are allowed", exception.getMessage());

	        // Verify repository save was **never** called
	        verify(userRepository, never()).save(any(User.class));
	    }

	    @Test
	    void testGetUserById_Success() {
	        // Mock user data
	        String userId = "12345";
	        User mockUser = new User(userId, "John Doe", "john.doe@example.com", 25, "France", "1234567890");

	        // Mock repository behavior
	        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

	        // Call service method
	        UserResponseDto responseDto = userService.getUserById(userId);

	        // Assertions
	        assertNotNull(responseDto);
	        assertEquals(userId, responseDto.getId());
	        assertEquals("John Doe", responseDto.getName());
	        assertEquals("john.doe@example.com", responseDto.getEmail());
	        assertEquals(25, responseDto.getAge());
	        assertEquals("France", responseDto.getCountry());
	        assertEquals("1234567890", responseDto.getPhoneNumber());

	        // Verify repository findById was called
	        verify(userRepository, times(1)).findById(userId);
	    }

	    @Test
	    void testGetUserById_NotFound() {
	        // Mock repository behavior (returning empty)
	        String userId = "99999";
	        when(userRepository.findById(userId)).thenReturn(Optional.empty());

	        // Expect RuntimeException
	        Exception exception = assertThrows(RuntimeException.class, () -> {
	            userService.getUserById(userId);
	        });

	        // Verify the message
	        assertEquals("User not found", exception.getMessage());

	        // Verify repository findById was **called once**
	        verify(userRepository, times(1)).findById(userId);
	    }
}
