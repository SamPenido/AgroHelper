package com.agrohelper.service.impl;

import com.agrohelper.dao.ProductDAO;
import com.agrohelper.dao.UserDAO;
import com.agrohelper.entity.Product;
import com.agrohelper.entity.Product.ProductCategory;
import com.agrohelper.entity.User;
import com.agrohelper.service.ProductService;
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
 * Implementação da camada de serviço para produtos
 */
@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    
    private final ProductDAO productDAO;
    private final UserDAO userDAO;

    @Autowired
    public ProductServiceImpl(ProductDAO productDAO, UserDAO userDAO) {
        this.productDAO = productDAO;
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public Map<String, Object> createProduct(Product product, Long userId) {
        logger.info("Service: Iniciando criação de produto para usuário ID {}", userId);
        Map<String, Object> response = new HashMap<>();
        
        // Buscar o usuário
        Optional<User> userOptional = userDAO.findById(userId);
        
        if (userOptional.isEmpty()) {
            logger.warn("Service: Usuário ID {} não encontrado", userId);
            response.put("success", false);
            response.put("message", "Usuário não encontrado");
            return response;
        }
        
        // Associar o usuário ao produto
        User user = userOptional.get();
        product.setUser(user);
        
        // Configurar a data de criação
        product.setCreatedAt(LocalDateTime.now());
        
        // Salvar o produto
        logger.info("Service: Salvando produto '{}'", product.getTitle());
        Product savedProduct = productDAO.save(product);
        
        // Criar resposta
        response.put("success", true);
        response.put("message", "Produto cadastrado com sucesso");
        response.put("product", savedProduct);
        
        logger.info("Service: Produto ID {} criado com sucesso", savedProduct.getId());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        logger.info("Service: Buscando produto com ID {}", id);
        Optional<Product> product = productDAO.findById(id);
        
        if (product.isPresent()) {
            logger.info("Service: Produto ID {} encontrado", id);
        } else {
            logger.info("Service: Produto ID {} não encontrado", id);
        }
        
        return product;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        logger.info("Service: Buscando todos os produtos");
        List<Product> products = productDAO.findAll();
        logger.info("Service: {} produtos encontrados", products.size());
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByUserId(Long userId) {
        logger.info("Service: Buscando produtos do usuário ID {}", userId);
        List<Product> products = productDAO.findByUserId(userId);
        logger.info("Service: {} produtos encontrados para o usuário ID {}", products.size(), userId);
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(ProductCategory category) {
        logger.info("Service: Buscando produtos da categoria {}", category);
        List<Product> products = productDAO.findByCategory(category);
        logger.info("Service: {} produtos encontrados na categoria {}", products.size(), category);
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> searchProducts(String searchTerm) {
        logger.info("Service: Buscando produtos com termo '{}'", searchTerm);
        List<Product> products = productDAO.findByTitleContainingOrDescriptionContaining(searchTerm, searchTerm);
        logger.info("Service: {} produtos encontrados para o termo '{}'", products.size(), searchTerm);
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByLocation(String location) {
        logger.info("Service: Buscando produtos da localização '{}'", location);
        List<Product> products = productDAO.findByLocationContaining(location);
        logger.info("Service: {} produtos encontrados na localização '{}'", products.size(), location);
        return products;
    }

    @Override
    @Transactional
    public Product saveProduct(Product product) {
        logger.info("Service: Salvando produto");
        Product savedProduct = productDAO.save(product);
        logger.info("Service: Produto ID {} salvo com sucesso", savedProduct.getId());
        return savedProduct;
    }
}