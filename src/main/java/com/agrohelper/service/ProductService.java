package com.agrohelper.service;

import com.agrohelper.entity.Product;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface de Serviço para operações relacionadas a produtos
 */
public interface ProductService {
    
    /**
     * Busca todos os produtos
     * @return Mapa com informações dos produtos e metadados
     */
    Map<String, Object> findAllProducts();
    
    /**
     * Busca um produto pelo ID
     * @param id ID do produto
     * @return Produto encontrado (ou vazio se não existir)
     */
    Optional<Product> findProductById(Long id);
    
    /**
     * Salva um novo produto
     * @param product Produto a ser salvo
     * @param userId ID do usuário proprietário
     * @return Produto salvo
     */
    Product saveProduct(Product product, Long userId);
    
    /**
     * Atualiza um produto existente
     * @param id ID do produto a ser atualizado
     * @param product Produto com dados atualizados
     * @return Produto atualizado
     */
    Product updateProduct(Long id, Product product);
    
    /**
     * Remove um produto pelo ID
     * @param id ID do produto a ser removido
     * @return true se removido com sucesso, false caso contrário
     */
    boolean deleteProduct(Long id);
    
    /**
     * Busca produtos por palavra-chave no título
     * @param keyword Palavra-chave para busca
     * @return Lista de produtos encontrados
     */
    List<Product> searchProductsByKeyword(String keyword);
    
    /**
     * Busca produtos por categoria
     * @param category Categoria de produtos
     * @return Lista de produtos encontrados
     */
    List<Product> findProductsByCategory(Product.ProductCategory category);
    
    /**
     * Busca produtos de um usuário específico
     * @param userId ID do usuário
     * @return Lista de produtos do usuário
     */
    List<Product> findProductsByUserId(Long userId);
}