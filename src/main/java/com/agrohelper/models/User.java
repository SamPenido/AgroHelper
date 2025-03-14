package com.agrohelper.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(nullable = false)
    private String passwordHash;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserType userType;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters e Setters
}

enum UserType {
    FARMER, TECHNICIAN, BUYER
}
