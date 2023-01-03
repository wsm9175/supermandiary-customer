package com.lodong.spring.supermandiarycustomer.repository;

import com.lodong.spring.supermandiarycustomer.domain.constructor.ConstructorWorkArea;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConstructorWorkAreaRepository extends JpaRepository<ConstructorWorkArea, String> {
    @EntityGraph(value = "get-with-all-sigg", type = EntityGraph.EntityGraphType.LOAD)
    public List<ConstructorWorkArea> findBySiggAreas_Code(int siggCode);
}
