package com.agrohelper.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Crop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "farm_id", nullable = false)
    private Farm farm;
    
    @Column(nullable = false, length = 50)
    private String cropType;
    
    @Column(precision = 10, scale = 2)
    private Double plantedArea;
    
    private LocalDate plantingDate;
    private LocalDate harvestDate;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CropStatus status = CropStatus.ACTIVE;

    // Getters e Setters
}

enum CropStatus {
    ACTIVE, HARVESTED, CANCELED
}
