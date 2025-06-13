package com.agrohelper.dao.impl;

import com.agrohelper.dao.ReviewDAO;
import com.agrohelper.entity.Review;
import com.agrohelper.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementação do DAO para acesso aos dados de avaliações
 */
@Repository
public class ReviewDAOImpl implements ReviewDAO {

    private static final Logger logger = LoggerFactory.getLogger(ReviewDAOImpl.class);
    
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewDAOImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review save(Review review) {
        logger.info("DAO: Salvando avaliação do produto ID: {} pelo usuário ID: {}", 
                review.getProduct() != null ? review.getProduct().getId() : null,
                review.getUser() != null ? review.getUser().getId() : null);
        return reviewRepository.save(review);
    }

    @Override
    public Optional<Review> findById(Long id) {
        logger.info("DAO: Buscando avaliação com ID: {}", id);
        return reviewRepository.findById(id);
    }

    @Override
    public List<Review> findAll() {
        logger.info("DAO: Buscando todas as avaliações");
        return reviewRepository.findAll();
    }

    @Override
    public List<Review> findByIsApprovedTrue() {
        logger.info("DAO: Buscando todas as avaliações aprovadas");
        return reviewRepository.findByIsApprovedTrue();
    }

    @Override
    public List<Review> findByProductId(Long productId) {
        logger.info("DAO: Buscando avaliações do produto ID: {}", productId);
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public List<Review> findByProductIdAndIsApprovedTrue(Long productId) {
        logger.info("DAO: Buscando avaliações aprovadas do produto ID: {}", productId);
        return reviewRepository.findByProductIdAndIsApprovedTrue(productId);
    }

    @Override
    public List<Review> findByUserId(Long userId) {
        logger.info("DAO: Buscando avaliações do usuário ID: {}", userId);
        return reviewRepository.findByUserId(userId);
    }

    @Override
    public boolean existsByProductIdAndUserId(Long productId, Long userId) {
        logger.info("DAO: Verificando se usuário ID: {} já avaliou o produto ID: {}", userId, productId);
        return reviewRepository.existsByProductIdAndUserId(productId, userId);
    }

    @Override
    public long countByProductId(Long productId) {
        logger.info("DAO: Contando avaliações do produto ID: {}", productId);
        return reviewRepository.countByProductId(productId);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("DAO: Removendo avaliação com ID: {}", id);
        reviewRepository.deleteById(id);
    }
}