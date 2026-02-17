package com.storefront.services;

import com.storefront.entities.Items;
import com.storefront.entities.Reviews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;

public interface ReviewService {
    Reviews createReview(String itemId, Reviews review);
    List<Reviews> getReviewsByItem(String itemId);
    Page<Reviews> getReviewsByItem(String itemId, Pageable pageable);
    Reviews updateReview(Long reviewId, Reviews updatedReview);
    void deleteReview(Long reviewId);
    double getAverageRatingForItem(String itemId);
    long getReviewCountForItem(String itemId);
    Map<Integer, Long> getRatingDistribution(String itemId);
}