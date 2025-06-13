package com.agrohelper.dao.impl;

import com.agrohelper.dao.ProductDAO;
import com.agrohelper.entity.Product;
import com.agrohelper.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementação DAO para acesso a dados de produtos
 * Utiliza o JpaRepository como fonte de dados
 */
@Repository
public class ProductDAOImpl implements ProductDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductDAOImpl.class);
    
    @Autowired
    private ProductRepository productRepository;
    
    @Override
    public List<Product> findAll() {
        logger.info("DAO: Buscando todos os produtos no banco de dados");
        List<Product> products = productRepository.findAll();
        logger.info("DAO: {} produtos recuperados do banco de dados", products.size());
        return products;
    }
    
    @Override
    public Optional<Product> findById(Long id) {
        logger.info("DAO: Buscando produto com ID {} no banco de dados", id);
        Optional<Product> product = productRepository.findById(id);
        
        if (product.isPresent()) {
            logger.info("DAO: Produto encontrado no banco: ID={}, Título={}", 
                    id, product.get().getTitle());
        } else {
            logger.info("DAO: Produto com ID {} não encontrado no banco de dados", id);
        }
        
        return product;
    }
    
    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            logger.info("DAO: Inserindo novo produto no banco de dados: {}", product.getTitle());
        } else {
            logger.info("DAO: Atualizando produto no banco de dados: ID={}, Título={}", 
                    product.getId(), product.getTitle());
        }
        
        Product savedProduct = productRepository.save(product);
        logger.info("DAO: Produto salvo com sucesso no banco de dados. ID gerado: {}", 
                savedProduct.getId());
        
        return savedProduct;
    }
    
    @Override
    public boolean existsById(Long id) {
        logger.info("DAO: Verificando existência do produto com ID {} no banco de dados", id);
        boolean exists = productRepository.existsById(id);
        
        if (exists) {
            logger.info("DAO: Produto com ID {} existe no banco de dados", id);
        } else {
            logger.info("DAO: Produto com ID {} não existe no banco de dados", id);
        }
        
        return exists;
    }
    
    @Override
    public void deleteById(Long id) {
        logger.info("DAO: Removendo produto com ID {} do banco de dados", id);
        productRepository.deleteById(id);
        logger.info("DAO: Produto com ID {} removido do banco de dados", id);
    }
    
    @Override
    public List<Product> findByCategory(Product.ProductCategory category) {
        logger.info("DAO: Buscando produtos da categoria {} no banco de dados", category);
        List<Product> products = productRepository.findByCategory(category);
        logger.info("DAO: {} produtos da categoria {} encontrados no banco de dados", 
                products.size(), category);
        
        return products;
    }
    
    @Override
    public List<Product> findByTitleContainingIgnoreCase(String title) {
        logger.info("DAO: Buscando produtos com título contendo '{}' no banco de dados", title);
        List<Product> products = productRepository.findByTitleContainingIgnoreCase(title);
        logger.info("DAO: {} produtos encontrados no banco de dados com título contendo '{}'", 
                products.size(), title);
        
        return products;
    }
    
    @Override
    public List<Product> findByUserIdOrderByCreatedAtDesc(Long userId) {
        logger.info("DAO: Buscando produtos do usuário ID {} no banco de dados", userId);
        List<Product> products = productRepository.findByUserIdOrderByCreatedAtDesc(userId);
        logger.info("DAO: {} produtos do usuário ID {} encontrados no banco de dados", 
                products.size(), userId);
        
        return products;
    }
}