package com.agrohelper.service;

import com.agrohelper.entity.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    /**
     * Registra um novo usuário no sistema
     * @param user O usuário a ser registrado
     * @return Os dados do usuário registrado
     */
    Map<String, Object> registerUser(User user);
    
    /**
     * Realiza o login de um usuário
     * @param email O email do usuário
     * @param password A senha do usuário
     * @return Os dados do usuário logado
     */
    Map<String, Object> loginUser(String email, String password);
    
    /**
     * Busca um usuário pelo ID
     * @param id ID do usuário
     * @return O usuário encontrado
     */
    Optional<User> getUserById(Long id);
    
    /**
     * Busca um usuário pelo email
     * @param email Email do usuário
     * @return O usuário encontrado
     */
    Optional<User> getUserByEmail(String email);
    
    /**
     * Lista todos os usuários cadastrados
     * @return Lista de usuários
     */
    List<User> getAllUsers();
    
    /**
     * Verifica se um email já está em uso
     * @param email Email a ser verificado
     * @return true se o email já estiver em uso, false caso contrário
     */
    boolean emailExists(String email);
    
    /**
     * Salva um usuário
     * @param user Usuário a ser salvo
     * @return Usuário salvo
     */
    User saveUser(User user);
}