package com.agrohelper.dao;

import com.agrohelper.entity.Product;
import com.agrohelper.entity.Product.ProductCategory;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO para acesso aos dados de produtos
 */
public interface ProductDAO {
    
    /**
     * Salva um produto no banco de dados
     * @param product Produto a ser salvo
     * @return Produto salvo com ID gerado
     */
    Product save(Product product);
    
    /**
     * Busca um produto pelo ID
     * @param id ID do produto
     * @return Produto encontrado ou vazio
     */
    Optional<Product> findById(Long id);
    
    /**
     * Lista todos os produtos
     * @return Lista de produtos
     */
    List<Product> findAll();
    
    /**
     * Lista produtos de um usuário específico
     * @param userId ID do usuário
     * @return Lista de produtos do usuário
     */
    List<Product> findByUserId(Long userId);
    
    /**
     * Busca produtos por categoria
     * @param category Categoria dos produtos
     * @return Lista de produtos da categoria
     */
    List<Product> findByCategory(ProductCategory category);
    
    /**
     * Busca produtos que contenham o termo de pesquisa no título ou descrição
     * @param titleTerm Termo de pesquisa para o título
     * @param descriptionTerm Termo de pesquisa para a descrição
     * @return Lista de produtos correspondentes
     */
    List<Product> findByTitleContainingOrDescriptionContaining(String titleTerm, String descriptionTerm);
    
    /**
     * Busca produtos por localização
     * @param location Localização a ser pesquisada
     * @return Lista de produtos da localização
     */
    List<Product> findByLocationContaining(String location);
    
    /**
     * Remove um produto pelo ID
     * @param id ID do produto a remover
     */
    void deleteById(Long id);
}