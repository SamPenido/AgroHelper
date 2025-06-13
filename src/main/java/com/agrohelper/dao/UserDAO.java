package com.agrohelper.dao;

import com.agrohelper.entity.User;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO para acesso aos dados de usuários
 */
public interface UserDAO {
    
    /**
     * Salva um usuário no banco de dados
     * @param user Usuário a ser salvo
     * @return Usuário salvo com ID gerado
     */
    User save(User user);
    
    /**
     * Busca um usuário pelo ID
     * @param id ID do usuário
     * @return Usuário encontrado ou vazio
     */
    Optional<User> findById(Long id);
    
    /**
     * Busca um usuário pelo email
     * @param email Email do usuário
     * @return Usuário encontrado ou vazio
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Lista todos os usuários
     * @return Lista de usuários
     */
    List<User> findAll();
    
    /**
     * Verifica se existe um usuário com o email informado
     * @param email Email a verificar
     * @return true se existir, false caso contrário
     */
    boolean existsByEmail(String email);
    
    /**
     * Remove um usuário pelo ID
     * @param id ID do usuário a remover
     */
    void deleteById(Long id);
}