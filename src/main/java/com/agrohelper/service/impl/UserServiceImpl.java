package com.agrohelper.service.impl;

import com.agrohelper.dao.UserDAO;
import com.agrohelper.entity.User;
import com.agrohelper.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementação da camada de serviço para usuários
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    private final UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public Map<String, Object> registerUser(User user) {
        logger.info("Service: Iniciando registro de usuário com email: {}", user.getEmail());
        Map<String, Object> response = new HashMap<>();
        
        // Verificar se o email já existe
        if (emailExists(user.getEmail())) {
            logger.warn("Service: Email {} já está em uso", user.getEmail());
            response.put("success", false);
            response.put("message", "Email já cadastrado");
            return response;
        }

        // Configurar a data de criação
        user.setCreatedAt(LocalDateTime.now());
        
        // Salvar o usuário
        logger.info("Service: Salvando usuário");
        User savedUser = userDAO.save(user);
        
        // Criar resposta
        Map<String, Object> userData = new HashMap<>();
        
        userData.put("id", savedUser.getFormattedId());
        userData.put("numericId", savedUser.getId());
        userData.put("email", savedUser.getEmail());
        userData.put("fullName", savedUser.getFullName());
        userData.put("userType", savedUser.getUserType().toString());
        
        response.put("success", true);
        response.put("message", "Usuário registrado com sucesso");
        response.put("user", userData);
        
        logger.info("Service: Usuário ID {} registrado com sucesso", savedUser.getId());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> loginUser(String email, String password) {
        logger.info("Service: Iniciando login para o email: {}", email);
        Map<String, Object> response = new HashMap<>();
        
        // Buscar o usuário pelo email
        Optional<User> userOptional = userDAO.findByEmail(email);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // Verificar a senha
            if (password.equals(user.getPassword())) {
                logger.info("Service: Login bem-sucedido para o email: {}", email);
                
                Map<String, Object> userData = new HashMap<>();
                
                userData.put("id", user.getFormattedId());
                userData.put("numericId", user.getId());
                userData.put("email", user.getEmail());
                userData.put("fullName", user.getFullName());
                userData.put("userType", user.getUserType().toString());
                
                response.put("success", true);
                response.put("message", "Login realizado com sucesso");
                response.put("user", userData);
                
                return response;
            } else {
                logger.warn("Service: Senha incorreta para o email: {}", email);
                response.put("success", false);
                response.put("message", "Senha incorreta");
                return response;
            }
        }
        
        // Usuário não encontrado
        logger.warn("Service: Email não encontrado: {}", email);
        response.put("success", false);
        response.put("message", "Email não cadastrado");
        
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        logger.info("Service: Buscando usuário com ID {}", id);
        Optional<User> user = userDAO.findById(id);
        
        if (user.isPresent()) {
            logger.info("Service: Usuário ID {} encontrado", id);
        } else {
            logger.info("Service: Usuário ID {} não encontrado", id);
        }
        
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        logger.info("Service: Buscando usuário com email {}", email);
        return userDAO.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        logger.info("Service: Buscando todos os usuários");
        List<User> users = userDAO.findAll();
        logger.info("Service: {} usuários encontrados", users.size());
        return users;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        logger.info("Service: Verificando se o email {} já existe", email);
        boolean exists = userDAO.existsByEmail(email);
        logger.info("Service: Email {} {}", email, exists ? "já existe" : "não existe");
        return exists;
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        logger.info("Service: Salvando usuário");
        User savedUser = userDAO.save(user);
        logger.info("Service: Usuário ID {} salvo com sucesso", savedUser.getId());
        return savedUser;
    }
}