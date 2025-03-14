package com.agrohelper.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Farm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(length = 100)
    private String location;
    
    @Column(precision = 10, scale = 2)
    private Double areaHectares;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters e Setters
}
