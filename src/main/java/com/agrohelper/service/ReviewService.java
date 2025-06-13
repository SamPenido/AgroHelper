package com.agrohelper.service;

import com.agrohelper.entity.Review;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface de serviço para operações relacionadas a avaliações
 */
public interface ReviewService {
    
    /**
     * Cria uma nova avaliação
     * @param review Avaliação a ser criada
     * @param productId ID do produto
     * @param userId ID do usuário que está avaliando
     * @return Mapa com resultado da operação
     */
    Map<String, Object> createReview(Review review, Long productId, Long userId);
    
    /**
     * Busca uma avaliação pelo ID
     * @param id ID da avaliação
     * @return A avaliação encontrada ou vazio
     */
    Optional<Review> getReviewById(Long id);
    
    /**
     * Lista todas as avaliações
     * @return Lista de avaliações
     */
    List<Review> getAllReviews();
    
    /**
     * Lista todas as avaliações aprovadas
     * @return Lista de avaliações aprovadas
     */
    List<Review> getApprovedReviews();
    
    /**
     * Lista avaliações de um produto específico
     * @param productId ID do produto
     * @return Lista de avaliações do produto
     */
    List<Review> getReviewsByProductId(Long productId);
    
    /**
     * Lista avaliações aprovadas de um produto específico
     * @param productId ID do produto
     * @return Lista de avaliações aprovadas do produto
     */
    List<Review> getApprovedReviewsByProductId(Long productId);
    
    /**
     * Lista avaliações de um usuário específico
     * @param userId ID do usuário
     * @return Lista de avaliações do usuário
     */
    List<Review> getReviewsByUserId(Long userId);
    
    /**
     * Verifica se um usuário já avaliou um produto
     * @param productId ID do produto
     * @param userId ID do usuário
     * @return true se o usuário já avaliou o produto, false caso contrário
     */
    boolean hasUserReviewedProduct(Long productId, Long userId);
    
    /**
     * Aprova uma avaliação
     * @param reviewId ID da avaliação
     * @return Avaliação aprovada
     */
    Map<String, Object> approveReview(Long reviewId);
    
    /**
     * Remove uma avaliação
     * @param reviewId ID da avaliação
     * @param userId ID do usuário que está solicitando a remoção
     * @return Resultado da operação
     */
    Map<String, Object> deleteReview(Long reviewId, Long userId);
}