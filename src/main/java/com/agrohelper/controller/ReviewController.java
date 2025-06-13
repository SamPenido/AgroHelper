package com.agrohelper.controller;

import com.agrohelper.entity.Review;
import com.agrohelper.entity.User;
import com.agrohelper.entity.UserType;
import com.agrohelper.service.ReviewService;
import com.agrohelper.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller para Review - Avaliações de produtos
 */
@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private UserService userService;

    // GET /reviews - Listar todas as avaliações (apenas admin)
    @GetMapping
    public ResponseEntity<?> getAllReviews(@RequestParam(required = false) Long userId) {
        try {
            logger.info("Controller: Recebida requisição para listar avaliações");
            
            // Verificar se o usuário é admin
            if (userId != null) {
                Optional<User> userOpt = userService.getUserById(userId);
                if (userOpt.isPresent() && userOpt.get().getUserType() != UserType.ADMIN) {
                    logger.warn("Controller: Acesso negado para usuário ID {}", userId);
                    return ResponseEntity.status(403).body(Map.of(
                        "success", false,
                        "message", "Apenas administradores podem listar todas as avaliações"
                    ));
                }
            }
            
            List<Review> reviews = reviewService.getAllReviews();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", reviews.size());
            response.put("reviews", reviews);
            
            logger.info("Controller: Retornando {} avaliações", reviews.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Controller: Erro ao buscar avaliações", e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao buscar avaliações: " + e.getMessage()
            ));
        }
    }

    // GET /reviews/product/{productId} - Listar avaliações de um produto
    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getReviewsByProduct(@PathVariable Long productId) {
        try {
            logger.info("Controller: Recebida requisição para listar avaliações do produto ID {}", productId);
            
            List<Review> reviews = reviewService.getApprovedReviewsByProductId(productId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", reviews.size());
            response.put("productId", productId);
            response.put("reviews", reviews);
            
            logger.info("Controller: Retornando {} avaliações para o produto ID {}", reviews.size(), productId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Controller: Erro ao buscar avaliações do produto", e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao buscar avaliações: " + e.getMessage()
            ));
        }
    }

    // GET /reviews/user/{userId} - Listar avaliações de um usuário
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReviewsByUser(@PathVariable Long userId, @RequestParam(required = false) Long requesterId) {
        try {
            logger.info("Controller: Recebida requisição para listar avaliações do usuário ID {}", userId);
            
            // Verificar se é o próprio usuário ou um admin
            if (requesterId != null && !userId.equals(requesterId)) {
                Optional<User> requesterOpt = userService.getUserById(requesterId);
                if (requesterOpt.isPresent() && requesterOpt.get().getUserType() != UserType.ADMIN) {
                    logger.warn("Controller: Acesso negado para usuário ID {}", requesterId);
                    return ResponseEntity.status(403).body(Map.of(
                        "success", false,
                        "message", "Você não tem permissão para ver avaliações de outros usuários"
                    ));
                }
            }
            
            List<Review> reviews = reviewService.getReviewsByUserId(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", reviews.size());
            response.put("userId", userId);
            response.put("reviews", reviews);
            
            logger.info("Controller: Retornando {} avaliações para o usuário ID {}", reviews.size(), userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Controller: Erro ao buscar avaliações do usuário", e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao buscar avaliações: " + e.getMessage()
            ));
        }
    }

    // POST /reviews - Criar nova avaliação
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Review review, 
                                          @RequestParam Long productId, 
                                          @RequestParam Long userId) {
        try {
            logger.info("Controller: Recebida requisição para criar avaliação para produto ID {} pelo usuário ID {}", 
                    productId, userId);
            
            // Verificar se o usuário é comprador
            Optional<User> userOpt = userService.getUserById(userId);
            if (userOpt.isPresent() && !userOpt.get().isBuyer()) {
                logger.warn("Controller: Acesso negado para usuário ID {}", userId);
                return ResponseEntity.status(403).body(Map.of(
                    "success", false,
                    "message", "Apenas compradores podem avaliar produtos"
                ));
            }
            
            Map<String, Object> result = reviewService.createReview(review, productId, userId);
            
            if ((boolean) result.get("success")) {
                logger.info("Controller: Avaliação criada com sucesso");
                return ResponseEntity.ok(result);
            } else {
                logger.warn("Controller: Falha ao criar avaliação: {}", result.get("message"));
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            logger.error("Controller: Erro ao criar avaliação", e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao criar avaliação: " + e.getMessage()
            ));
        }
    }

    // POST /reviews/{reviewId}/approve - Aprovar uma avaliação (apenas admin)
    @PostMapping("/{reviewId}/approve")
    public ResponseEntity<?> approveReview(@PathVariable Long reviewId, @RequestParam Long userId) {
        try {
            logger.info("Controller: Recebida requisição para aprovar avaliação ID {} pelo usuário ID {}", 
                    reviewId, userId);
            
            // Verificar se o usuário é admin
            Optional<User> userOpt = userService.getUserById(userId);
            if (userOpt.isPresent() && !userOpt.get().isAdmin()) {
                logger.warn("Controller: Acesso negado para usuário ID {}", userId);
                return ResponseEntity.status(403).body(Map.of(
                    "success", false,
                    "message", "Apenas administradores podem aprovar avaliações"
                ));
            }
            
            Map<String, Object> result = reviewService.approveReview(reviewId);
            
            if ((boolean) result.get("success")) {
                logger.info("Controller: Avaliação aprovada com sucesso");
                return ResponseEntity.ok(result);
            } else {
                logger.warn("Controller: Falha ao aprovar avaliação: {}", result.get("message"));
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            logger.error("Controller: Erro ao aprovar avaliação", e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao aprovar avaliação: " + e.getMessage()
            ));
        }
    }

    // DELETE /reviews/{reviewId} - Remover uma avaliação
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId, @RequestParam Long userId) {
        try {
            logger.info("Controller: Recebida requisição para remover avaliação ID {} pelo usuário ID {}", 
                    reviewId, userId);
            
            Map<String, Object> result = reviewService.deleteReview(reviewId, userId);
            
            if ((boolean) result.get("success")) {
                logger.info("Controller: Avaliação removida com sucesso");
                return ResponseEntity.ok(result);
            } else {
                logger.warn("Controller: Falha ao remover avaliação: {}", result.get("message"));
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            logger.error("Controller: Erro ao remover avaliação", e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao remover avaliação: " + e.getMessage()
            ));
        }
    }

    // GET /reviews/check - Verificar se um usuário já avaliou um produto
    @GetMapping("/check")
    public ResponseEntity<?> checkUserReview(@RequestParam Long productId, @RequestParam Long userId) {
        try {
            logger.info("Controller: Verificando se usuário ID {} já avaliou o produto ID {}", userId, productId);
            
            boolean hasReviewed = reviewService.hasUserReviewedProduct(productId, userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("hasReviewed", hasReviewed);
            
            logger.info("Controller: Usuário {} já avaliou o produto: {}", userId, hasReviewed);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Controller: Erro ao verificar avaliação", e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao verificar avaliação: " + e.getMessage()
            ));
        }
    }
}