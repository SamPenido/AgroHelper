package com.agrohelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Classe principal da aplicação AgroHelper
 * Sistema inteligente para gestão agrícola
 * 
 * Implementa o padrão de camadas:
 * Controller -> Service -> DAO -> Repository -> Database
 * 
 * @author AgroHelper Team
 */
@SpringBootApplication
@EnableTransactionManagement
public class AgroHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgroHelperApplication.class, args);
        System.out.println("🌱 AgroHelper Backend iniciado!");
        System.out.println("📊 API: http://localhost:8080/api/v1");
        System.out.println("🧩 Arquitetura em camadas implementada:");
        System.out.println("   Controller -> Service -> DAO -> Repository -> Database");
    }
}