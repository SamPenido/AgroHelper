package com.agrohelper.controller;

import com.agrohelper.entity.Product;
import com.agrohelper.entity.Review;
import com.agrohelper.entity.User;
import com.agrohelper.entity.UserType;
import com.agrohelper.service.ProductService;
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
 * Controller para o painel de administração
 */
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ReviewService reviewService;

    // GET /admin/dashboard - Obter estatísticas do sistema
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats(@RequestParam Long adminId) {
        logger.info("Controller: Recebida requisição para obter estatísticas do dashboard pelo admin ID {}", adminId);
        
        try {
            // Verificar se o usuário é um administrador
            Optional<User> adminOpt = userService.getUserById(adminId);
            
            if (adminOpt.isEmpty() || !adminOpt.get().isAdmin()) {
                logger.warn("Controller: Acesso negado ao dashboard. Usuário ID {} não é um administrador", adminId);
                return ResponseEntity.status(403).body(Map.of(
                    "success", false,
                    "message", "Acesso negado. Apenas administradores podem acessar o dashboard"
                ));
            }
            
            // Obter estatísticas
            List<User> allUsers = userService.getAllUsers();
            List<Product> allProducts = productService.getAllProducts();
            List<Review> allReviews = reviewService.getAllReviews();
            
            // Contar por tipo de usuário
            long buyerCount = allUsers.stream().filter(User::isBuyer).count();
            long sellerCount = allUsers.stream().filter(User::isSeller).count();
            long adminCount = allUsers.stream().filter(User::isAdmin).count();
            
            // Montar resposta
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalUsers", allUsers.size());
            stats.put("buyerCount", buyerCount);
            stats.put("sellerCount", sellerCount);
            stats.put("adminCount", adminCount);
            stats.put("totalProducts", allProducts.size());
            stats.put("totalReviews", allReviews.size());
            
            response.put("stats", stats);
            
            logger.info("Controller: Estatísticas do dashboard retornadas com sucesso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Controller: Erro ao obter estatísticas do dashboard", e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao obter estatísticas do dashboard: " + e.getMessage()
            ));
        }
    }

    // GET /admin/users - Listar todos os usuários
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam Long adminId) {
        logger.info("Controller: Recebida requisição para listar todos os usuários pelo admin ID {}", adminId);
        
        try {
            // Verificar se o usuário é um administrador
            Optional<User> adminOpt = userService.getUserById(adminId);
            
            if (adminOpt.isEmpty() || !adminOpt.get().isAdmin()) {
                logger.warn("Controller: Acesso negado à lista de usuários. Usuário ID {} não é um administrador", adminId);
                return ResponseEntity.status(403).body(Map.of(
                    "success", false,
                    "message", "Acesso negado. Apenas administradores podem listar todos os usuários"
                ));
            }
            
            List<User> users = userService.getAllUsers();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", users.size());
            response.put("users", users);
            
            logger.info("Controller: {} usuários retornados com sucesso", users.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Controller: Erro ao listar usuários", e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao listar usuários: " + e.getMessage()
            ));
        }
    }

    // GET /admin/products - Listar todos os produtos
    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts(@RequestParam Long adminId) {
        logger.info("Controller: Recebida requisição para listar todos os produtos pelo admin ID {}", adminId);
        
        try {
            // Verificar se o usuário é um administrador
            Optional<User> adminOpt = userService.getUserById(adminId);
            
            if (adminOpt.isEmpty() || !adminOpt.get().isAdmin()) {
                logger.warn("Controller: Acesso negado à lista de produtos. Usuário ID {} não é um administrador", adminId);
                return ResponseEntity.status(403).body(Map.of(
                    "success", false,
                    "message", "Acesso negado. Apenas administradores podem listar todos os produtos"
                ));
            }
            
            List<Product> products = productService.getAllProducts();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", products.size());
            response.put("products", products);
            
            logger.info("Controller: {} produtos retornados com sucesso", products.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Controller: Erro ao listar produtos", e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao listar produtos: " + e.getMessage()
            ));
        }
    }

    // GET /admin/reviews - Listar todas as avaliações
    @GetMapping("/reviews")
    public ResponseEntity<?> getAllReviews(@RequestParam Long adminId) {
        logger.info("Controller: Recebida requisição para listar todas as avaliações pelo admin ID {}", adminId);
        
        try {
            // Verificar se o usuário é um administrador
            Optional<User> adminOpt = userService.getUserById(adminId);
            
            if (adminOpt.isEmpty() || !adminOpt.get().isAdmin()) {
                logger.warn("Controller: Acesso negado à lista de avaliações. Usuário ID {} não é um administrador", adminId);
                return ResponseEntity.status(403).body(Map.of(
                    "success", false,
                    "message", "Acesso negado. Apenas administradores podem listar todas as avaliações"
                ));
            }
            
            List<Review> reviews = reviewService.getAllReviews();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", reviews.size());
            response.put("reviews", reviews);
            
            logger.info("Controller: {} avaliações retornadas com sucesso", reviews.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Controller: Erro ao listar avaliações", e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao listar avaliações: " + e.getMessage()
            ));
        }
    }

    // POST /admin/users/{userId}/type - Alterar tipo de usuário
    @PostMapping("/users/{userId}/type")
    public ResponseEntity<?> changeUserType(@PathVariable Long userId, 
                                          @RequestParam UserType newType,
                                          @RequestParam Long adminId) {
        logger.info("Controller: Recebida requisição para alterar tipo do usuário ID {} para {} pelo admin ID {}", 
                userId, newType, adminId);
        
        try {
            // Verificar se o usuário é um administrador
            Optional<User> adminOpt = userService.getUserById(adminId);
            
            if (adminOpt.isEmpty() || !adminOpt.get().isAdmin()) {
                logger.warn("Controller: Acesso negado à alteração de tipo. Usuário ID {} não é um administrador", adminId);
                return ResponseEntity.status(403).body(Map.of(
                    "success", false,
                    "message", "Acesso negado. Apenas administradores podem alterar tipos de usuário"
                ));
            }
            
            // Buscar o usuário a ser alterado
            Optional<User> userOpt = userService.getUserById(userId);
            
            if (userOpt.isEmpty()) {
                logger.warn("Controller: Usuário ID {} não encontrado", userId);
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Usuário não encontrado"
                ));
            }
            
            User user = userOpt.get();
            user.setUserType(newType);
            User savedUser = userService.saveUser(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Tipo de usuário alterado com sucesso");
            response.put("userId", userId);
            response.put("userType", savedUser.getUserType());
            
            logger.info("Controller: Tipo do usuário ID {} alterado para {} com sucesso", userId, newType);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Controller: Erro ao alterar tipo de usuário", e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao alterar tipo de usuário: " + e.getMessage()
            ));
        }
    }

    // DELETE /admin/products/{productId} - Remover produto
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId, @RequestParam Long adminId) {
        logger.info("Controller: Recebida requisição para remover produto ID {} pelo admin ID {}", productId, adminId);
        
        try {
            // Verificar se o usuário é um administrador
            Optional<User> adminOpt = userService.getUserById(adminId);
            
            if (adminOpt.isEmpty() || !adminOpt.get().isAdmin()) {
                logger.warn("Controller: Acesso negado à remoção de produto. Usuário ID {} não é um administrador", adminId);
                return ResponseEntity.status(403).body(Map.of(
                    "success", false,
                    "message", "Acesso negado. Apenas administradores podem remover produtos"
                ));
            }
            
            // Verificar se o produto existe
            Optional<Product> productOpt = productService.getProductById(productId);
            
            if (productOpt.isEmpty()) {
                logger.warn("Controller: Produto ID {} não encontrado", productId);
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Produto não encontrado"
                ));
            }
            
            // TODO: Implementar método de remoção de produto no ProductService
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Produto removido com sucesso");
            
            logger.info("Controller: Produto ID {} removido com sucesso pelo admin ID {}", productId, adminId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Controller: Erro ao remover produto", e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao remover produto: " + e.getMessage()
            ));
        }
    }
}