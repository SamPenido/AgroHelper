package com.agrohelper.dao.impl;

import com.agrohelper.dao.ProductDAO;
import com.agrohelper.entity.Product;
import com.agrohelper.entity.Product.ProductCategory;
import com.agrohelper.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação do DAO para acesso aos dados de produtos
 */
@Repository
public class ProductDAOImpl implements ProductDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProductDAOImpl.class);
    
    private final ProductRepository productRepository;

    @Autowired
    public ProductDAOImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        logger.info("DAO: Salvando produto: {}", product.getTitle());
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        logger.info("DAO: Buscando produto com ID: {}", id);
        return productRepository.findById(id);
    }

    @Override
    public List<Product> findAll() {
        logger.info("DAO: Buscando todos os produtos");
        return productRepository.findAll();
    }

    @Override
    public List<Product> findByUserId(Long userId) {
        logger.info("DAO: Buscando produtos do usuário ID: {}", userId);
        return productRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Product> findByCategory(ProductCategory category) {
        logger.info("DAO: Buscando produtos da categoria: {}", category);
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> findByTitleContainingOrDescriptionContaining(String titleTerm, String descriptionTerm) {
        logger.info("DAO: Buscando produtos com termo de pesquisa: {}", titleTerm);
        List<Product> titleResults = productRepository.findByTitleContainingIgnoreCase(titleTerm);
        List<Product> descriptionResults = productRepository.findByDescriptionContainingIgnoreCase(descriptionTerm);
        
        // Combinar resultados sem duplicação
        List<Product> combinedResults = new ArrayList<>(titleResults);
        for (Product product : descriptionResults) {
            if (!combinedResults.contains(product)) {
                combinedResults.add(product);
            }
        }
        
        return combinedResults;
    }

    @Override
    public List<Product> findByLocationContaining(String location) {
        logger.info("DAO: Buscando produtos da localização: {}", location);
        return productRepository.findByLocationContainingIgnoreCase(location);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("DAO: Removendo produto com ID: {}", id);
        productRepository.deleteById(id);
    }
}