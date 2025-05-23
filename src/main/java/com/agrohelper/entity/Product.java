package com.agrohelper.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade Product - Projeto Acadêmico Simplificado
 */
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Título é obrigatório")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Descrição é obrigatória")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    @Column(length = 100)
    private String location;

    @NotBlank(message = "Nome do vendedor é obrigatório")
    @Column(name = "seller_name", nullable = false)
    private String sellerName;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Enum para categorias
    public enum ProductCategory {
        GRAINS("Grãos"),
        FRUITS("Frutas"),
        VEGETABLES("Legumes"),
        EQUIPMENT("Equipamentos"),
        SERVICES("Serviços"),
        INPUTS("Insumos");

        private final String displayName;

        ProductCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Construtores
    public Product() {
        this.createdAt = LocalDateTime.now();
    }

    public Product(String title, String description, BigDecimal price, ProductCategory category, String sellerName) {
        this();
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.sellerName = sellerName;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", category=" + category +
                ", location='" + location + '\'' +
                ", sellerName='" + sellerName + '\'' +
                '}';
    }
}