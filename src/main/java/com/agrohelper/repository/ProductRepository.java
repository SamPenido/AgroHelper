package com.agrohelper.repository;

import com.agrohelper.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para Product - Projeto Acadêmico Simplificado
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByCategory(Product.ProductCategory category);
    
    List<Product> findByTitleContainingIgnoreCase(String title);
    
    List<Product> findByDescriptionContainingIgnoreCase(String description);
    
    List<Product> findByLocationContainingIgnoreCase(String location);
    
    List<Product> findByUserIdOrderByCreatedAtDesc(Long userId);
}