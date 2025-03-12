package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.*;


/**
 * Data Transfer Object for user registration requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Min(value = 18, message = "User must be at least 18 years old")
    private int age;

    @NotBlank(message = "Country is required")
    private String country;
    private String phoneNumber;
}
