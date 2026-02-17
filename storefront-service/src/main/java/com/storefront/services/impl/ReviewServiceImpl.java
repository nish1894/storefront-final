package com.storefront.services.impl;

import com.storefront.entities.Items;
import com.storefront.entities.Reviews;
import com.storefront.helpers.ResourceNotFoundException;
import com.storefront.repositories.ItemsRepo;
import com.storefront.repositories.ReviewsRepo;
import com.storefront.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewsRepo reviewsRepo;

    @Autowired
    private ItemsRepo itemsRepo;

    @Override
    @Transactional
    public Reviews createReview(String itemId, Reviews review) {
        Items item = itemsRepo.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        review.setItem(item);
        review.setUpdatedAt(LocalDateTime.now());
        return reviewsRepo.save(review);
    }

    @Override
    public List<Reviews> getReviewsByItem(String itemId) {
        Items item = itemsRepo.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
        return reviewsRepo.findByItem(item);
    }

    @Override
    public Page<Reviews> getReviewsByItem(String itemId, Pageable pageable) {
        Items item = itemsRepo.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
        return reviewsRepo.findByItem(item, pageable);
    }

    @Override
    @Transactional
    public Reviews updateReview(Long reviewId, Reviews updatedReview) {
        Reviews review = reviewsRepo.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        review.setRating(updatedReview.getRating());
        review.setComment(updatedReview.getComment());
        review.setUpdatedAt(LocalDateTime.now());

        return reviewsRepo.save(review);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        if (!reviewsRepo.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found");
        }
        reviewsRepo.deleteById(reviewId);
    }

    @Override
    public double getAverageRatingForItem(String itemId) {
        Items item = itemsRepo.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
        return reviewsRepo.getAverageRatingByItem(item);
    }

    @Override
    public long getReviewCountForItem(String itemId) {
        Items item = itemsRepo.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
        return reviewsRepo.countByItem(item);
    }

    @Override
    public Map<Integer, Long> getRatingDistribution(String itemId) {
        Items item = itemsRepo.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Item not found"));

        // Get raw distribution data
        List<Object[]> distribution = reviewsRepo.getRatingDistribution(item);
        
        // Create map with all possible ratings initialized to 0
        Map<Integer, Long> ratingMap = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            ratingMap.put(i, 0L);
        }
        
        // Fill in actual counts from the query results
        for (Object[] result : distribution) {
            Integer rating = (Integer) result[0];
            Long count = (Long) result[1];
            ratingMap.put(rating, count);
        }
        
        return ratingMap;
    }
}
