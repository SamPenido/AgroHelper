package com.agrohelper.controller;

import com.agrohelper.entity.User;
import com.agrohelper.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller para User - Projeto Acadêmico Refatorado
 * Implementa o padrão de camadas Controller -> Service -> DAO -> Repository
 */
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // POST /users/register - Registrar novo usuário
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        logger.info("Controller: Recebida requisição para registrar usuário com email: {}", user.getEmail());
        
        try {
            Map<String, Object> response = userService.registerUser(user);
            
            if ((boolean) response.get("success")) {
                logger.info("Controller: Usuário registrado com sucesso: {}", user.getEmail());
                return ResponseEntity.ok(response);
            } else {
                logger.warn("Controller: Falha ao registrar usuário: {}", response.get("message"));
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            logger.error("Controller: Erro ao registrar usuário", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro ao cadastrar usuário: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // POST /users/login - Login do usuário
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        logger.info("Controller: Recebida requisição de login para o email: {}", email);
        
        try {
            String password = loginData.get("password");
            Map<String, Object> response = userService.loginUser(email, password);
            
            if ((boolean) response.get("success")) {
                logger.info("Controller: Login bem-sucedido para o email: {}", email);
                return ResponseEntity.ok(response);
            } else {
                logger.warn("Controller: Falha no login para o email: {}", email);
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            logger.error("Controller: Erro no processo de login", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro no login: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}