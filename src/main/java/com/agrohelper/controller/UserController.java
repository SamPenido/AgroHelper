package com.agrohelper.controller;

import com.agrohelper.entity.User;
import com.agrohelper.entity.UserType;
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
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user, 
                                                        @RequestParam(required = false) UserType userType) {
        logger.info("Controller: Recebida requisição para registrar usuário com email: {}", user.getEmail());
        
        if (userType != null) {
            user.setUserType(userType);
        }
        
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
    
    // GET /users/type - Verificar o tipo de usuário
    @GetMapping("/type")
    public ResponseEntity<?> getUserType(@RequestParam Long userId) {
        logger.info("Controller: Recebida requisição para verificar tipo de usuário ID: {}", userId);
        
        try {
            Map<String, Object> response = new HashMap<>();
            
            // Buscar o usuário
            User user = userService.getUserById(userId).orElse(null);
            
            if (user == null) {
                logger.warn("Controller: Usuário ID: {} não encontrado", userId);
                response.put("success", false);
                response.put("message", "Usuário não encontrado");
                return ResponseEntity.badRequest().body(response);
            }
            
            response.put("success", true);
            response.put("userId", userId);
            response.put("userType", user.getUserType());
            response.put("isBuyer", user.isBuyer());
            response.put("isSeller", user.isSeller());
            response.put("isAdmin", user.isAdmin());
            
            logger.info("Controller: Tipo de usuário ID: {} verificado: {}", userId, user.getUserType());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Controller: Erro ao verificar tipo de usuário", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro ao verificar tipo de usuário: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // POST /users/{userId}/type - Alterar o tipo de usuário (admin only)
    @PostMapping("/{userId}/type")
    public ResponseEntity<?> changeUserType(@PathVariable Long userId, 
                                           @RequestParam UserType newType,
                                           @RequestParam Long adminId) {
        logger.info("Controller: Recebida requisição para alterar tipo de usuário ID: {} para: {}", userId, newType);
        
        try {
            Map<String, Object> response = new HashMap<>();
            
            // Verificar se o solicitante é admin
            User admin = userService.getUserById(adminId).orElse(null);
            
            if (admin == null || !admin.isAdmin()) {
                logger.warn("Controller: Acesso negado para alterar tipo de usuário. Solicitante ID: {} não é admin", adminId);
                response.put("success", false);
                response.put("message", "Apenas administradores podem alterar o tipo de usuário");
                return ResponseEntity.status(403).body(response);
            }
            
            // Buscar o usuário a ser alterado
            User user = userService.getUserById(userId).orElse(null);
            
            if (user == null) {
                logger.warn("Controller: Usuário ID: {} não encontrado", userId);
                response.put("success", false);
                response.put("message", "Usuário não encontrado");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Alterar o tipo
            user.setUserType(newType);
            User savedUser = userService.saveUser(user);
            
            response.put("success", true);
            response.put("message", "Tipo de usuário alterado com sucesso");
            response.put("userId", userId);
            response.put("userType", savedUser.getUserType());
            
            logger.info("Controller: Tipo de usuário ID: {} alterado para: {}", userId, newType);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Controller: Erro ao alterar tipo de usuário", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro ao alterar tipo de usuário: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}