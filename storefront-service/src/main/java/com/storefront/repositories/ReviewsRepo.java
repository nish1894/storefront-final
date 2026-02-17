package com.storefront.repositories;

import com.storefront.entities.Items;
import com.storefront.entities.Reviews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewsRepo extends JpaRepository<Reviews, Long> {
    
    // Find all reviews for a specific item
    List<Reviews> findByItem(Items item);
    
    // Find reviews with pagination
    Page<Reviews> findByItem(Items item, Pageable pageable);
    
    // Count total reviews for an item
    long countByItem(Items item);
    
    // Get average rating for an item
    @Query("SELECT COALESCE(AVG(r.rating), 0.0) FROM Reviews r WHERE r.item = ?1")
    double getAverageRatingByItem(Items item);
    
    // Find latest reviews first
    Page<Reviews> findByItemOrderByUpdatedAtDesc(Items item, Pageable pageable);
    
    // Find reviews by rating
    Page<Reviews> findByItemAndRating(Items item, int rating, Pageable pageable);
    
    // Search reviews by comment content
    @Query("SELECT r FROM Reviews r WHERE r.item = ?1 AND LOWER(r.comment) LIKE LOWER(CONCAT('%', ?2, '%'))")
    Page<Reviews> searchByComment(Items item, String keyword, Pageable pageable);
    
    // Get rating distribution for an item
    @Query("SELECT r.rating, COUNT(r) FROM Reviews r WHERE r.item = ?1 GROUP BY r.rating ORDER BY r.rating DESC")
    List<Object[]> getRatingDistribution(Items item);
}
