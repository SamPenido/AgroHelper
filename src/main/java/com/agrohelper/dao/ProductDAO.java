package com.agrohelper.dao;

import com.agrohelper.entity.Product;

import java.util.List;
import java.util.Optional;

/**
 * Interface DAO (Data Access Object) para operações de persistência de produtos
 */
public interface ProductDAO {
    
    /**
     * Busca todos os produtos
     * @return Lista de todos os produtos
     */
    List<Product> findAll();
    
    /**
     * Busca um produto pelo ID
     * @param id ID do produto
     * @return Produto encontrado (ou vazio se não existir)
     */
    Optional<Product> findById(Long id);
    
    /**
     * Salva um produto (novo ou existente)
     * @param product Produto a ser salvo
     * @return Produto salvo com ID gerado (se for novo)
     */
    Product save(Product product);
    
    /**
     * Verifica se um produto existe pelo ID
     * @param id ID do produto
     * @return true se existir, false caso contrário
     */
    boolean existsById(Long id);
    
    /**
     * Remove um produto pelo ID
     * @param id ID do produto a ser removido
     */
    void deleteById(Long id);
    
    /**
     * Busca produtos por categoria
     * @param category Categoria de produtos
     * @return Lista de produtos da categoria
     */
    List<Product> findByCategory(Product.ProductCategory category);
    
    /**
     * Busca produtos contendo texto no título (case insensitive)
     * @param title Texto a ser buscado no título
     * @return Lista de produtos que contêm o texto no título
     */
    List<Product> findByTitleContainingIgnoreCase(String title);
    
    /**
     * Busca produtos de um usuário específico ordenados por data de criação
     * @param userId ID do usuário
     * @return Lista de produtos do usuário
     */
    List<Product> findByUserIdOrderByCreatedAtDesc(Long userId);
}