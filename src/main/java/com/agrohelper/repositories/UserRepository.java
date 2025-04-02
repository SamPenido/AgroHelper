package com.agrohelper.repositories;

import com.agrohelper.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (used for login and checking duplicates)
    Optional<User> findByEmail(String email);

    // Check if an email already exists (used for registration)
    boolean existsByEmail(String email);
}
