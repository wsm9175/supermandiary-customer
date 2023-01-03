package com.lodong.spring.supermandiarycustomer.repository;

import com.lodong.spring.supermandiarycustomer.address.SiggAreas;
import com.lodong.spring.supermandiarycustomer.domain.constructor.Constructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConstructorRepository extends JpaRepository<Constructor, String> {
    @Override
    @EntityGraph(value = "get-constructor-detail", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Constructor> findById(String s);

    @EntityGraph(value = "get-all-data", type = EntityGraph.EntityGraphType.LOAD)
    public List<Constructor> findByConstructorWorkAreas(SiggAreas siggAreas);

    public Optional<List<Constructor>> findByNameContaining(String keyWord);

}
