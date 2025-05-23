package com.agrohelper.repository;

import com.agrohelper.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository para Product - Projeto Acadêmico Simplificado
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    
    List<Product> findByCategory(Product.ProductCategory category);
    
    List<Product> findByTitleContainingIgnoreCase(String title);
    
    List<Product> findByDescriptionContainingIgnoreCase(String description);
    
    List<Product> findByLocationContainingIgnoreCase(String location);
}