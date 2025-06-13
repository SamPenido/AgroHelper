package com.agrohelper.service.impl;

import com.agrohelper.dao.ProductDAO;
import com.agrohelper.dao.ReviewDAO;
import com.agrohelper.dao.UserDAO;
import com.agrohelper.entity.Product;
import com.agrohelper.entity.Review;
import com.agrohelper.entity.User;
import com.agrohelper.entity.UserType;
import com.agrohelper.service.ReviewService;
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
 * Implementação do serviço de avaliações
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);
    
    private final ReviewDAO reviewDAO;
    private final ProductDAO productDAO;
    private final UserDAO userDAO;

    @Autowired
    public ReviewServiceImpl(ReviewDAO reviewDAO, ProductDAO productDAO, UserDAO userDAO) {
        this.reviewDAO = reviewDAO;
        this.productDAO = productDAO;
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public Map<String, Object> createReview(Review review, Long productId, Long userId) {
        logger.info("Service: Iniciando criação de avaliação para produto ID: {} pelo usuário ID: {}", productId, userId);
        Map<String, Object> response = new HashMap<>();
        
        // Verificar se o usuário existe
        Optional<User> userOptional = userDAO.findById(userId);
        if (userOptional.isEmpty()) {
            logger.warn("Service: Usuário ID: {} não encontrado", userId);
            response.put("success", false);
            response.put("message", "Usuário não encontrado");
            return response;
        }
        
        User user = userOptional.get();
        
        // Apenas compradores podem avaliar produtos
        if (!user.isBuyer()) {
            logger.warn("Service: Usuário ID: {} não é um comprador e não pode avaliar produtos", userId);
            response.put("success", false);
            response.put("message", "Apenas compradores podem avaliar produtos");
            return response;
        }
        
        // Verificar se o produto existe
        Optional<Product> productOptional = productDAO.findById(productId);
        if (productOptional.isEmpty()) {
            logger.warn("Service: Produto ID: {} não encontrado", productId);
            response.put("success", false);
            response.put("message", "Produto não encontrado");
            return response;
        }
        
        Product product = productOptional.get();
        
        // Verificar se o usuário já avaliou este produto
        if (reviewDAO.existsByProductIdAndUserId(productId, userId)) {
            logger.warn("Service: Usuário ID: {} já avaliou o produto ID: {}", userId, productId);
            response.put("success", false);
            response.put("message", "Você já avaliou este produto");
            return response;
        }
        
        // Configurar a avaliação
        review.setUser(user);
        review.setProduct(product);
        review.setCreatedAt(LocalDateTime.now());
        review.setIsApproved(true); // Por padrão, avaliações já são aprovadas
        
        // Salvar a avaliação
        Review savedReview = reviewDAO.save(review);
        
        // Atualizar a média de avaliações do produto
        product.addReview(savedReview);
        productDAO.save(product);
        
        logger.info("Service: Avaliação criada com sucesso! ID: {}", savedReview.getId());
        
        response.put("success", true);
        response.put("message", "Avaliação enviada com sucesso");
        response.put("review", savedReview);
        
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Review> getReviewById(Long id) {
        logger.info("Service: Buscando avaliação com ID: {}", id);
        return reviewDAO.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getAllReviews() {
        logger.info("Service: Buscando todas as avaliações");
        return reviewDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getApprovedReviews() {
        logger.info("Service: Buscando todas as avaliações aprovadas");
        return reviewDAO.findByIsApprovedTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getReviewsByProductId(Long productId) {
        logger.info("Service: Buscando avaliações do produto ID: {}", productId);
        return reviewDAO.findByProductId(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getApprovedReviewsByProductId(Long productId) {
        logger.info("Service: Buscando avaliações aprovadas do produto ID: {}", productId);
        return reviewDAO.findByProductIdAndIsApprovedTrue(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getReviewsByUserId(Long userId) {
        logger.info("Service: Buscando avaliações do usuário ID: {}", userId);
        return reviewDAO.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserReviewedProduct(Long productId, Long userId) {
        logger.info("Service: Verificando se usuário ID: {} já avaliou o produto ID: {}", userId, productId);
        return reviewDAO.existsByProductIdAndUserId(productId, userId);
    }

    @Override
    @Transactional
    public Map<String, Object> approveReview(Long reviewId) {
        logger.info("Service: Aprovando avaliação ID: {}", reviewId);
        Map<String, Object> response = new HashMap<>();
        
        Optional<Review> reviewOptional = reviewDAO.findById(reviewId);
        
        if (reviewOptional.isEmpty()) {
            logger.warn("Service: Avaliação ID: {} não encontrada", reviewId);
            response.put("success", false);
            response.put("message", "Avaliação não encontrada");
            return response;
        }
        
        Review review = reviewOptional.get();
        review.setIsApproved(true);
        
        Review savedReview = reviewDAO.save(review);
        
        // Atualizar a média de avaliações do produto
        Product product = review.getProduct();
        product.updateAverageRating();
        productDAO.save(product);
        
        logger.info("Service: Avaliação ID: {} aprovada com sucesso", reviewId);
        
        response.put("success", true);
        response.put("message", "Avaliação aprovada com sucesso");
        response.put("review", savedReview);
        
        return response;
    }

    @Override
    @Transactional
    public Map<String, Object> deleteReview(Long reviewId, Long userId) {
        logger.info("Service: Solicitação para remover avaliação ID: {} pelo usuário ID: {}", reviewId, userId);
        Map<String, Object> response = new HashMap<>();
        
        // Buscar o usuário
        Optional<User> userOptional = userDAO.findById(userId);
        if (userOptional.isEmpty()) {
            logger.warn("Service: Usuário ID: {} não encontrado", userId);
            response.put("success", false);
            response.put("message", "Usuário não encontrado");
            return response;
        }
        
        User user = userOptional.get();
        
        // Buscar a avaliação
        Optional<Review> reviewOptional = reviewDAO.findById(reviewId);
        if (reviewOptional.isEmpty()) {
            logger.warn("Service: Avaliação ID: {} não encontrada", reviewId);
            response.put("success", false);
            response.put("message", "Avaliação não encontrada");
            return response;
        }
        
        Review review = reviewOptional.get();
        
        // Verificar permissões: apenas o próprio usuário que criou a avaliação ou um administrador pode removê-la
        if (!review.getUser().getId().equals(userId) && user.getUserType() != UserType.ADMIN) {
            logger.warn("Service: Usuário ID: {} não tem permissão para remover a avaliação ID: {}", userId, reviewId);
            response.put("success", false);
            response.put("message", "Você não tem permissão para remover esta avaliação");
            return response;
        }
        
        // Remover a avaliação do produto
        Product product = review.getProduct();
        product.removeReview(review);
        
        // Salvar o produto com a avaliação atualizada
        productDAO.save(product);
        
        // Remover a avaliação
        reviewDAO.deleteById(reviewId);
        
        logger.info("Service: Avaliação ID: {} removida com sucesso", reviewId);
        
        response.put("success", true);
        response.put("message", "Avaliação removida com sucesso");
        
        return response;
    }
}