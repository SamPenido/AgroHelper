package com.agrohelper.repository;

import com.agrohelper.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para Review
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByProductId(Long productId);
    
    List<Review> findByUserId(Long userId);
    
    List<Review> findByProductIdAndUserId(Long productId, Long userId);
    
    List<Review> findByIsApprovedTrue();
    
    List<Review> findByProductIdAndIsApprovedTrue(Long productId);
    
    long countByProductId(Long productId);
    
    boolean existsByProductIdAndUserId(Long productId, Long userId);
}