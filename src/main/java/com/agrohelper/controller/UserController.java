package com.agrohelper.controller;

import com.agrohelper.entity.User;
import com.agrohelper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controller para User - Projeto Acadêmico Simplificado
 */
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // POST /users/register - Registrar novo usuário
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Verificar se email já existe
            if (userRepository.existsByEmail(user.getEmail())) {
                response.put("success", false);
                response.put("message", "Email já cadastrado!");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Salvar usuário (senha em texto plano para simplicidade acadêmica)
            User savedUser = userRepository.save(user);
            
            response.put("success", true);
            response.put("message", "Usuário cadastrado com sucesso!");
            response.put("user", Map.of(
                "id", savedUser.getId(),
                "email", savedUser.getEmail(),
                "fullName", savedUser.getFullName()
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao cadastrar usuário: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // POST /users/login - Login do usuário
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String email = loginData.get("email");
            String password = loginData.get("password");
            
            Optional<User> userOpt = userRepository.findByEmail(email);
            
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Email não encontrado!");
                return ResponseEntity.badRequest().body(response);
            }
            
            User user = userOpt.get();
            
            // Verificar senha (texto plano para simplicidade acadêmica)
            if (!user.getPassword().equals(password)) {
                response.put("success", false);
                response.put("message", "Senha incorreta!");
                return ResponseEntity.badRequest().body(response);
            }
            
            response.put("success", true);
            response.put("message", "Login realizado com sucesso!");
            response.put("user", Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "fullName", user.getFullName()
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro no login: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}