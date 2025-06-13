package com.agrohelper.dao;

import com.agrohelper.entity.Review;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO para acesso aos dados de avaliações
 */
public interface ReviewDAO {
    
    /**
     * Salva uma avaliação no banco de dados
     * @param review Avaliação a ser salva
     * @return Avaliação salva com ID gerado
     */
    Review save(Review review);
    
    /**
     * Busca uma avaliação pelo ID
     * @param id ID da avaliação
     * @return Avaliação encontrada ou vazio
     */
    Optional<Review> findById(Long id);
    
    /**
     * Lista todas as avaliações
     * @return Lista de avaliações
     */
    List<Review> findAll();
    
    /**
     * Lista todas as avaliações aprovadas
     * @return Lista de avaliações aprovadas
     */
    List<Review> findByIsApprovedTrue();
    
    /**
     * Lista avaliações de um produto específico
     * @param productId ID do produto
     * @return Lista de avaliações do produto
     */
    List<Review> findByProductId(Long productId);
    
    /**
     * Lista avaliações aprovadas de um produto específico
     * @param productId ID do produto
     * @return Lista de avaliações aprovadas do produto
     */
    List<Review> findByProductIdAndIsApprovedTrue(Long productId);
    
    /**
     * Lista avaliações de um usuário específico
     * @param userId ID do usuário
     * @return Lista de avaliações do usuário
     */
    List<Review> findByUserId(Long userId);
    
    /**
     * Verifica se um usuário já avaliou um produto
     * @param productId ID do produto
     * @param userId ID do usuário
     * @return true se o usuário já avaliou o produto, false caso contrário
     */
    boolean existsByProductIdAndUserId(Long productId, Long userId);
    
    /**
     * Conta o número de avaliações de um produto
     * @param productId ID do produto
     * @return Número de avaliações
     */
    long countByProductId(Long productId);
    
    /**
     * Remove uma avaliação pelo ID
     * @param id ID da avaliação a remover
     */
    void deleteById(Long id);
}