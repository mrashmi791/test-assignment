package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;


/**
 * Service class to handle user management
 */
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	public UserResponseDto registerUser(UserRequestDto requestDto) {
		if (!"France".equalsIgnoreCase(requestDto.getCountry())) {
			throw new IllegalArgumentException("Only users from France are allowed");
		}

		User user = User.builder().name(requestDto.getName()).email(requestDto.getEmail()).age(requestDto.getAge())
				.country(requestDto.getCountry()).phoneNumber(requestDto.getPhoneNumber()) // Optional field
				.build();

		User savedUser = userRepository.save(user);
		return new UserResponseDto(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getAge(),
				savedUser.getCountry(), savedUser.getPhoneNumber());
	}

	public UserResponseDto getUserById(String id) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
		return new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getCountry(),
				user.getPhoneNumber());
	}
}