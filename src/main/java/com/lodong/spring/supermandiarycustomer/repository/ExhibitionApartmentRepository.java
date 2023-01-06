package com.lodong.spring.supermandiarycustomer.repository;

import com.lodong.spring.supermandiarycustomer.domain.exhibition.ExhibitionApartment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExhibitionApartmentRepository extends JpaRepository<ExhibitionApartment, String> {
    @EntityGraph(value = "exhibition-apartment-get-all", type = EntityGraph.EntityGraphType.LOAD)
    public Optional<List<ExhibitionApartment>> findByApartment_SiggAreas_CodeIn(List<Integer> code);


}
