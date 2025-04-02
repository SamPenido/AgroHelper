package com.agrohelper.repositories;

import com.agrohelper.models.Product;
import com.agrohelper.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find active products for the marketplace view
    List<Product> findByActiveTrueOrderByCreatedAtDesc();

    // Find active products by a specific seller (for dashboard/user profile)
    List<Product> findBySellerAndActiveTrueOrderByCreatedAtDesc(User seller);

    // Optional: Find active products by category
    List<Product> findByCategoryAndActiveTrueOrderByCreatedAtDesc(String category);
}
