package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST Controller for managing user registration and retrieval.
 */

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    /**
     * Registers a new user.
     * Logs input request, output response, and processing time.
     *
     * @param requestDto The user request data.
     * @return The registered user's response data.
     */
    
    @PostMapping("/register")
    public UserResponseDto registerUser(@Valid @RequestBody UserRequestDto requestDto) {
    	 long startTime = System.currentTimeMillis();
         logger.info("Received register request: {}", requestDto);
         UserResponseDto response  = userService.registerUser(requestDto);
         long endTime = System.currentTimeMillis();
         logger.info("User registered successfully: {} | Processing time: {} ms", response, (endTime - startTime));
         
        return response;
        		
    }

    /**
     * Retrieves a registered user by their unique identifier.
     * Logs input request, output response, and processing time.
     *
     * @param id The unique identifier of the user.
     * @return The user details.
     */
    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable String id) {
    	   long startTime = System.currentTimeMillis();
           logger.info("Fetching user with ID: {}", id);
           
           UserResponseDto response = userService.getUserById(id);
           
           long endTime = System.currentTimeMillis();
           logger.info("User details retrieved: {} | Processing time: {} ms", response, (endTime - startTime));
           
           return response;

    }
}