package com.agrohelper.services;

import com.agrohelper.dto.ProductRequestDTO;
import com.agrohelper.models.Product;
import com.agrohelper.models.User;
import com.agrohelper.repositories.ProductRepository;
import com.agrohelper.repositories.UserRepository; // Para buscar o vendedor
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository; // Para associar o produto ao usuário

    @Autowired
    public ProductService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Product createProduct(ProductRequestDTO productDTO, Long sellerId) {
        // Busca o usuário vendedor pelo ID
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado com ID: " + sellerId));

        Product newProduct = new Product();
        newProduct.setTitle(productDTO.getTitle());
        newProduct.setDescription(productDTO.getDescription());
        newProduct.setPrice(productDTO.getPrice());
        newProduct.setCategory(productDTO.getCategory());
        newProduct.setSeller(seller); // Associa o produto ao vendedor
        newProduct.setActive(true); // Produto ativo por padrão
        // createdAt e updatedAt são gerenciados pelo model/JPA

        return productRepository.save(newProduct);
    }

    // Lista todos os produtos ativos para o marketplace
    public List<Product> listActiveProducts() {
        return productRepository.findByActiveTrueOrderByCreatedAtDesc();
    }

    // Lista produtos ativos de um vendedor específico (para dashboard)
    public List<Product> listUserActiveProducts(Long sellerId) {
         User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado com ID: " + sellerId));
        return productRepository.findBySellerAndActiveTrueOrderByCreatedAtDesc(seller);
    }

     // Busca um produto por ID (para página de detalhes)
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
               .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + productId));
    }
}
