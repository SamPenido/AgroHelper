package com.agrohelper.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuração principal da aplicação
 * Define os componentes e configurações de injeção de dependência
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {
    "com.agrohelper.controller",
    "com.agrohelper.service",
    "com.agrohelper.service.impl",
    "com.agrohelper.dao",
    "com.agrohelper.dao.impl",
    "com.agrohelper.repository"
})
public class AppConfig {
    // Configurações adicionais podem ser adicionadas aqui
}