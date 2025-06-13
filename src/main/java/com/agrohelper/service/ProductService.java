package com.agrohelper.service;

import com.agrohelper.entity.Product;
import com.agrohelper.entity.Product.ProductCategory;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {
    /**
     * Cria um novo produto
     * @param product Produto a ser criado
     * @param userId ID do usuário proprietário
     * @return Produto criado
     */
    Map<String, Object> createProduct(Product product, Long userId);
    
    /**
     * Busca um produto pelo ID
     * @param id ID do produto
     * @return O produto encontrado
     */
    Optional<Product> getProductById(Long id);
    
    /**
     * Lista todos os produtos
     * @return Lista de produtos
     */
    List<Product> getAllProducts();
    
    /**
     * Lista produtos de um usuário específico
     * @param userId ID do usuário
     * @return Lista de produtos do usuário
     */
    List<Product> getProductsByUserId(Long userId);
    
    /**
     * Busca produtos por categoria
     * @param category Categoria dos produtos
     * @return Lista de produtos da categoria
     */
    List<Product> getProductsByCategory(ProductCategory category);
    
    /**
     * Busca produtos por termo de pesquisa no título ou descrição
     * @param searchTerm Termo de pesquisa
     * @return Lista de produtos correspondentes
     */
    List<Product> searchProducts(String searchTerm);
    
    /**
     * Busca produtos por localização
     * @param location Localização a ser pesquisada
     * @return Lista de produtos da localização
     */
    List<Product> getProductsByLocation(String location);
    
    /**
     * Salva um produto
     * @param product Produto a ser salvo
     * @return Produto salvo
     */
    Product saveProduct(Product product);
}