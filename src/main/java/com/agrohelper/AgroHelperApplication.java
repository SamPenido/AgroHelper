package com.agrohelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação AgroHelper
 * Sistema inteligente para gestão agrícola
 * 
 * @author AgroHelper Team
 */
@SpringBootApplication
public class AgroHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgroHelperApplication.class, args);
        System.out.println("🌱 AgroHelper Backend iniciado!");
        System.out.println("📊 API: http://localhost:8080/api/v1");
    }
}