package com.lodong.spring.supermandiarycustomer.repository;

import com.lodong.spring.supermandiarycustomer.domain.estimate.Estimate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstimateRepository extends JpaRepository<Estimate, String> {
    @Override
    @EntityGraph(value = "get-with-requestorder-product", type = EntityGraph.EntityGraphType.LOAD)
    public Optional<Estimate> findById(String id);
    @EntityGraph(value = "get-with-requestorder-product", type = EntityGraph.EntityGraphType.LOAD)
    public Optional<List<Estimate>> findByRequestOrder_Customer_Id(String customerId);
}
