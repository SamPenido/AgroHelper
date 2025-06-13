package com.agrohelper.service.impl;

import com.agrohelper.dao.ProductDAO;
import com.agrohelper.entity.Product;
import com.agrohelper.entity.User;
import com.agrohelper.repository.UserRepository;
import com.agrohelper.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementação do serviço de produtos
 */
@Service
public class ProductServiceImpl implements ProductService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    
    @Autowired
    private ProductDAO productDAO;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public Map<String, Object> findAllProducts() {
        logger.info("Service: Buscando todos os produtos");
        
        List<Product> products = productDAO.findAll();
        
        // Criar mapa de resposta com metadados
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("products", products);
        response.put("count", products.size());
        
        logger.info("Service: Encontrados {} produtos", products.size());
        return response;
    }
    
    @Override
    public Optional<Product> findProductById(Long id) {
        logger.info("Service: Buscando produto com ID {}", id);
        Optional<Product> product = productDAO.findById(id);
        
        if (product.isPresent()) {
            logger.info("Service: Produto encontrado: {}", product.get().getTitle());
        } else {
            logger.info("Service: Produto com ID {} não encontrado", id);
        }
        
        return product;
    }
    
    @Override
    public Product saveProduct(Product product, Long userId) {
        logger.info("Service: Iniciando cadastro de novo produto: {} para o usuário ID {}", 
                  product.getTitle(), userId);
        
        // Buscar o usuário
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.error("Service: Usuário com ID {} não encontrado", userId);
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        
        // Associar o usuário ao produto
        product.setUser(userOpt.get());
        
        // Salvar o produto através do DAO
        Product savedProduct = productDAO.save(product);
        logger.info("Service: Produto salvo com sucesso. ID: {}", savedProduct.getId());
        
        return savedProduct;
    }
    
    @Override
    public Product updateProduct(Long id, Product product) {
        logger.info("Service: Atualizando produto com ID {}", id);
        
        // Verificar se o produto existe
        if (!productDAO.existsById(id)) {
            logger.error("Service: Produto com ID {} não encontrado para atualização", id);
            throw new IllegalArgumentException("Produto não encontrado");
        }
        
        // Garantir que o ID não seja alterado
        product.setId(id);
        
        // Salvar as alterações
        Product updatedProduct = productDAO.save(product);
        logger.info("Service: Produto atualizado com sucesso: {}", updatedProduct.getTitle());
        
        return updatedProduct;
    }
    
    @Override
    public boolean deleteProduct(Long id) {
        logger.info("Service: Removendo produto com ID {}", id);
        
        // Verificar se o produto existe
        if (!productDAO.existsById(id)) {
            logger.error("Service: Produto com ID {} não encontrado para remoção", id);
            return false;
        }
        
        // Remover o produto
        productDAO.deleteById(id);
        logger.info("Service: Produto com ID {} removido com sucesso", id);
        
        return true;
    }
    
    @Override
    public List<Product> searchProductsByKeyword(String keyword) {
        logger.info("Service: Buscando produtos com a palavra-chave: '{}'", keyword);
        
        List<Product> products = productDAO.findByTitleContainingIgnoreCase(keyword);
        logger.info("Service: Encontrados {} produtos com a palavra-chave '{}'", 
                  products.size(), keyword);
        
        return products;
    }
    
    @Override
    public List<Product> findProductsByCategory(Product.ProductCategory category) {
        logger.info("Service: Buscando produtos da categoria: {}", category);
        
        List<Product> products = productDAO.findByCategory(category);
        logger.info("Service: Encontrados {} produtos na categoria {}", 
                  products.size(), category);
        
        return products;
    }
    
    @Override
    public List<Product> findProductsByUserId(Long userId) {
        logger.info("Service: Buscando produtos do usuário ID {}", userId);
        
        List<Product> products = productDAO.findByUserIdOrderByCreatedAtDesc(userId);
        logger.info("Service: Encontrados {} produtos do usuário ID {}", 
                  products.size(), userId);
        
        return products;
    }
}