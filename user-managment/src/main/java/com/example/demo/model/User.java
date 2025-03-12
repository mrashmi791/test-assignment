package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
 
	  	@Id
	    private String id;

	    @NotBlank(message = "Name is required")
	    private String name;

	    @Email(message = "Invalid email format")
	    @NotBlank(message = "Email is required")
	    private String email;

	    @Min(value = 18, message = "User must be at least 18 years old")
	    private int age;

	    @NotBlank(message = "Country is required")
	    private String country;
	    
	 // Optional field
	    private String phoneNumber;
}
