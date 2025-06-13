package com.agrohelper.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade Product - Projeto Acadêmico
 */
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_id_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

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
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating;
    
    @Column(name = "review_count")
    private Integer reviewCount;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

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
        this.averageRating = BigDecimal.ZERO;
        this.reviewCount = 0;
    }

    public Product(String title, String description, BigDecimal price, ProductCategory category, String sellerName, User user) {
        this();
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.sellerName = sellerName;
        this.user = user;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    // Formata o ID para ter 6 dígitos com zeros à esquerda (ex: 000001)
    public String getFormattedId() {
        if (id == null) return null;
        return String.format("%06d", id);
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
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
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
    
    public BigDecimal getAverageRating() {
        return averageRating;
    }
    
    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }
    
    public Integer getReviewCount() {
        return reviewCount;
    }
    
    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }
    
    public List<Review> getReviews() {
        return reviews;
    }
    
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
    
    // Método para adicionar uma nova avaliação e atualizar a média
    public void addReview(Review review) {
        if (!this.reviews.contains(review)) {
            this.reviews.add(review);
            review.setProduct(this);
            updateAverageRating();
        }
    }
    
    // Método para remover uma avaliação e atualizar a média
    public void removeReview(Review review) {
        if (this.reviews.contains(review)) {
            this.reviews.remove(review);
            review.setProduct(null);
            updateAverageRating();
        }
    }
    
    // Método para atualizar a média de avaliações
    public void updateAverageRating() {
        if (this.reviews.isEmpty()) {
            this.averageRating = BigDecimal.ZERO;
            this.reviewCount = 0;
            return;
        }
        
        int sum = 0;
        for (Review review : this.reviews) {
            sum += review.getRating();
        }
        
        this.reviewCount = this.reviews.size();
        this.averageRating = new BigDecimal(sum).divide(new BigDecimal(this.reviewCount), 2, BigDecimal.ROUND_HALF_UP);
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
                ", userId=" + (user != null ? user.getFormattedId() : null) +
                ", averageRating=" + averageRating +
                ", reviewCount=" + reviewCount +
                '}';
    }
}