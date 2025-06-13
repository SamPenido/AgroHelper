package com.agrohelper.controller;

import com.agrohelper.entity.Product;
import com.agrohelper.entity.Product.ProductCategory;
import com.agrohelper.entity.User;
import com.agrohelper.service.ProductService;
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
 * Controller para Product - Projeto Acadêmico Refatorado
 * Implementa o padrão de camadas Controller -> Service -> DAO -> Repository
 */
@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserService userService;

    // GET /products - Listar todos os produtos
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        try {
            logger.info("Controller: Recebida requisição para listar todos os produtos");
            List<Product> products = productService.getAllProducts();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", products.size());
            response.put("products", products);
            
            logger.info("Controller: Retornando {} produtos", products.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Controller: Erro ao buscar produtos", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Erro ao buscar produtos: " + e.getMessage());
            error.put("error", e.getClass().getSimpleName());
            return ResponseEntity.status(500).body(error);
        }
    }

    // GET /products/{id} - Buscar produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        logger.info("Controller: Recebida requisição para buscar produto com ID {}", id);
        Optional<Product> productOpt = productService.getProductById(id);
        
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            logger.info("Controller: Produto encontrado e retornado: {}", product.getTitle());
            
            // Criar um mapa com os dados do produto e incluir explicitamente o userId
            Map<String, Object> productData = new HashMap<>();
            productData.put("id", product.getId());
            productData.put("title", product.getTitle());
            productData.put("description", product.getDescription());
            productData.put("price", product.getPrice());
            productData.put("category", product.getCategory());
            productData.put("location", product.getLocation());
            productData.put("sellerName", product.getSellerName());
            productData.put("imageUrl", product.getImageUrl());
            productData.put("createdAt", product.getCreatedAt());
            productData.put("averageRating", product.getAverageRating());
            productData.put("reviewCount", product.getReviewCount());
            
            // Adicionar o ID do usuário dono do produto
            if (product.getUser() != null) {
                productData.put("userId", product.getUser().getId());
            }
            
            return ResponseEntity.ok(productData);
        } else {
            logger.info("Controller: Produto com ID {} não encontrado", id);
            return ResponseEntity.notFound().build();
        }
    }

    // POST /products - Criar novo produto
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product, @RequestParam Long userId) {
        try {
            logger.info("Controller: Recebida requisição para criar produto '{}' para usuário ID {}", 
                    product.getTitle(), userId);
            
            // Verificar se o usuário é um vendedor
            User user = userService.getUserById(userId).orElse(null);
            
            if (user == null) {
                logger.warn("Controller: Usuário ID {} não encontrado", userId);
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Usuário não encontrado");
                return ResponseEntity.badRequest().body(error);
            }
            
            if (!user.isSeller()) {
                logger.warn("Controller: Usuário ID {} não é um vendedor", userId);
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Apenas vendedores podem criar produtos");
                return ResponseEntity.status(403).body(error);
            }
            
            Map<String, Object> result = productService.createProduct(product, userId);
            
            if ((boolean) result.get("success")) {
                logger.info("Controller: Produto criado com sucesso");
                return ResponseEntity.ok(result);
            } else {
                logger.warn("Controller: Falha ao criar produto: {}", result.get("message"));
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            logger.error("Controller: Erro ao criar produto", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Erro ao criar produto: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // GET /products/category/{category} - Busca por categoria
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable ProductCategory category) {
        logger.info("Controller: Recebida requisição para buscar produtos da categoria {}", category);
        List<Product> products = productService.getProductsByCategory(category);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", products.size());
        response.put("category", category);
        response.put("products", products);
        
        logger.info("Controller: Retornando {} produtos da categoria {}", products.size(), category);
        return ResponseEntity.ok(response);
    }
    
    // GET /products/user/{userId} - Busca produtos de um usuário específico
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getProductsByUserId(@PathVariable Long userId) {
        logger.info("Controller: Recebida requisição para buscar produtos do usuário ID {}", userId);
        
        // Verificar se o usuário existe
        User user = userService.getUserById(userId).orElse(null);
        
        if (user == null) {
            logger.warn("Controller: Usuário ID {} não encontrado", userId);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Usuário não encontrado");
            return ResponseEntity.badRequest().body(error);
        }
        
        // Verificar se o usuário é vendedor
        if (!user.isSeller()) {
            logger.warn("Controller: Usuário ID {} não é um vendedor", userId);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Este usuário não é um vendedor");
            return ResponseEntity.badRequest().body(error);
        }
        
        List<Product> products = productService.getProductsByUserId(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", products.size());
        response.put("userId", userId);
        response.put("sellerName", user.getFullName());
        response.put("products", products);
        
        logger.info("Controller: Retornando {} produtos do usuário ID {}", products.size(), userId);
        return ResponseEntity.ok(response);
    }
    
    // GET /products/search - Busca por termo
    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam String term) {
        logger.info("Controller: Recebida requisição para buscar produtos com termo '{}'", term);
        List<Product> products = productService.searchProducts(term);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", products.size());
        response.put("searchTerm", term);
        response.put("products", products);
        
        logger.info("Controller: Retornando {} produtos para o termo '{}'", products.size(), term);
        return ResponseEntity.ok(response);
    }
    
    // GET /products/location - Busca por localização
    @GetMapping("/location")
    public ResponseEntity<?> getProductsByLocation(@RequestParam String location) {
        logger.info("Controller: Recebida requisição para buscar produtos da localização '{}'", location);
        List<Product> products = productService.getProductsByLocation(location);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", products.size());
        response.put("location", location);
        response.put("products", products);
        
        logger.info("Controller: Retornando {} produtos da localização '{}'", products.size(), location);
        return ResponseEntity.ok(response);
    }
    
    // DELETE /products/{id} - Remover um produto
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, @RequestParam Long userId) {
        logger.info("Controller: Recebida requisição para remover produto ID {} pelo usuário ID {}", id, userId);
        
        try {
            // Verificar se o produto existe
            Optional<Product> productOpt = productService.getProductById(id);
            
            if (productOpt.isEmpty()) {
                logger.warn("Controller: Produto ID {} não encontrado", id);
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Produto não encontrado");
                return ResponseEntity.notFound().build();
            }
            
            Product product = productOpt.get();
            
            // Verificar se o usuário é o dono do produto ou um admin
            User user = userService.getUserById(userId).orElse(null);
            
            if (user == null) {
                logger.warn("Controller: Usuário ID {} não encontrado", userId);
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Usuário não encontrado");
                return ResponseEntity.badRequest().body(error);
            }
            
            boolean isOwner = product.getUser().getId().equals(userId);
            boolean isAdmin = user.isAdmin();
            
            if (!isOwner && !isAdmin) {
                logger.warn("Controller: Usuário ID {} não tem permissão para remover o produto ID {}", userId, id);
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Você não tem permissão para remover este produto");
                return ResponseEntity.status(403).body(error);
            }
            
            // Remover produto
            boolean removed = productService.deleteProduct(id);
            
            if (!removed) {
                logger.warn("Controller: Falha ao remover o produto ID {}", id);
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Falha ao remover o produto");
                return ResponseEntity.badRequest().body(error);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Produto removido com sucesso");
            
            logger.info("Controller: Produto ID {} removido com sucesso", id);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Controller: Erro ao remover produto", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Erro ao remover produto: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}