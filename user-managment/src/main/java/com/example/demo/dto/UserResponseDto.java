package com.example.demo.dto;

import lombok.*;

/**
 * Data Transfer Object for user response data.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String id;
    private String name;
    private String email;
    private int age;
    private String country;
    private String phoneNumber;
}
