package com.lodong.spring.supermandiarycustomer.repository;

import com.lodong.spring.supermandiarycustomer.domain.review.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, String> {
    public Optional<ReviewLike> findByReview_IdAndUserCustomer_Id(String reviewId, String userId);
    public void deleteById(String id);
}
