package com.agrohelper.controller;

import com.agrohelper.entity.Product;
import com.agrohelper.service.ProductService;
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
 * Controller para Product - Projeto Acadêmico Simplificado
 */
@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    // GET /products - Listar todos os produtos
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        try {
            logger.info("Controller: Recebida requisição para listar todos os produtos");
            Map<String, Object> response = productService.findAllProducts();
            logger.info("Controller: Retornando {} produtos", response.get("count"));
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
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        logger.info("Controller: Recebida requisição para buscar produto com ID {}", id);
        Optional<Product> product = productService.findProductById(id);
        
        if (product.isPresent()) {
            logger.info("Controller: Produto encontrado e retornado: {}", product.get().getTitle());
            return ResponseEntity.ok(product.get());
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
            
            Product savedProduct = productService.saveProduct(product, userId);
            
            logger.info("Controller: Produto criado com sucesso. ID: {}", savedProduct.getId());
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            logger.error("Controller: Erro ao criar produto", e);
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", "Erro ao criar produto: " + e.getMessage()));
        }
    }

    // PUT /products/{id} - Atualizar produto
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            logger.info("Controller: Recebida requisição para atualizar produto com ID {}", id);
            Product updatedProduct = productService.updateProduct(id, product);
            logger.info("Controller: Produto atualizado com sucesso: {}", updatedProduct.getTitle());
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            logger.error("Controller: Produto com ID {} não encontrado para atualização", id);
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /products/{id} - Deletar produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        logger.info("Controller: Recebida requisição para remover produto com ID {}", id);
        boolean deleted = productService.deleteProduct(id);
        
        if (deleted) {
            logger.info("Controller: Produto com ID {} removido com sucesso", id);
            return ResponseEntity.ok().build();
        } else {
            logger.error("Controller: Produto com ID {} não encontrado para remoção", id);
            return ResponseEntity.notFound().build();
        }
    }

    // GET /products/search?keyword= - Busca por palavra-chave
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        logger.info("Controller: Recebida requisição para buscar produtos com palavra-chave '{}'", keyword);
        List<Product> products = productService.searchProductsByKeyword(keyword);
        logger.info("Controller: Retornando {} produtos para a palavra-chave '{}'", products.size(), keyword);
        return ResponseEntity.ok(products);
    }

    // GET /products/category/{category} - Busca por categoria
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Product.ProductCategory category) {
        logger.info("Controller: Recebida requisição para buscar produtos da categoria {}", category);
        List<Product> products = productService.findProductsByCategory(category);
        logger.info("Controller: Retornando {} produtos da categoria {}", products.size(), category);
        return ResponseEntity.ok(products);
    }
    
    // GET /products/user/{userId} - Busca produtos de um usuário específico
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Product>> getProductsByUserId(@PathVariable Long userId) {
        logger.info("Controller: Recebida requisição para buscar produtos do usuário ID {}", userId);
        List<Product> products = productService.findProductsByUserId(userId);
        logger.info("Controller: Retornando {} produtos do usuário ID {}", products.size(), userId);
        return ResponseEntity.ok(products);
    }
}