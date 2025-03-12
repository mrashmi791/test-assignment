package com.example.demo.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.User;

/**
 * Repository interface for managing user data in MongoDB.
 */

public interface UserRepository extends MongoRepository<User, String> {}
