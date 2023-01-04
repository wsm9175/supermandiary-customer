package com.lodong.spring.supermandiarycustomer.repository;

import com.lodong.spring.supermandiarycustomer.domain.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, String> {
    @EntityGraph(value = "get-review-with-all", type = EntityGraph.EntityGraphType.LOAD)
    public Optional<List<Review>> findByCustomer_Id(String customerId);

    @EntityGraph(value = "get-review-with-all", type = EntityGraph.EntityGraphType.LOAD)
    public Page<Review> findByConstructor_Id(String constructorId, Pageable pageable);
}
