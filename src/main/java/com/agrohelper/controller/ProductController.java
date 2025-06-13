package com.agrohelper.controller;

import com.agrohelper.entity.Product;
import com.agrohelper.entity.User;
import com.agrohelper.repository.ProductRepository;
import com.agrohelper.repository.UserRepository;
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

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;

    // GET /products - Listar todos os produtos
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            // Retorna um objeto JSON com uma propriedade 'products' para debugging
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("products", products);
            response.put("count", products.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
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
        Optional<Product> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // POST /products - Criar novo produto
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product, @RequestParam Long userId) {
        try {
            // Buscar o usuário pelo ID
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Usuário não encontrado"));
            }
            
            // Associar o usuário ao produto
            product.setUser(userOpt.get());
            
            Product savedProduct = productRepository.save(product);
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", "Erro ao criar produto: " + e.getMessage()));
        }
    }

    // PUT /products/{id} - Atualizar produto
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        product.setId(id);
        Product updatedProduct = productRepository.save(product);
        return ResponseEntity.ok(updatedProduct);
    }

    // DELETE /products/{id} - Deletar produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        productRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // GET /products/search?keyword= - Busca por palavra-chave
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productRepository.findByTitleContainingIgnoreCase(keyword);
        return ResponseEntity.ok(products);
    }

    // GET /products/category/{category} - Busca por categoria
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Product.ProductCategory category) {
        List<Product> products = productRepository.findByCategory(category);
        return ResponseEntity.ok(products);
    }
    
    // GET /products/user/{userId} - Busca produtos de um usuário específico
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Product>> getProductsByUserId(@PathVariable Long userId) {
        List<Product> products = productRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(products);
    }
}