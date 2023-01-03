package com.lodong.spring.supermandiarycustomer.repository;

import com.lodong.spring.supermandiarycustomer.domain.working.Working;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkingRepository extends JpaRepository<Working, String> {
    @EntityGraph(value = "get-estimate-info", type = EntityGraph.EntityGraphType.LOAD)
    public Optional<List<Working>> findByUserCustomer_Id(String id);

    @EntityGraph(value = "get-estimate-info", type = EntityGraph.EntityGraphType.LOAD)
    public Optional<List<Working>> findByApartment_IdAndApartmentDongAndApartmentHosuAndUserCustomer_Id(String apartmentId, String dong, String hosu, String uuid);
    @EntityGraph(value = "get-estimate-info", type = EntityGraph.EntityGraphType.LOAD)
    public Optional<List<Working>> findByOtherHome_IdAndOtherHomeDongAndOtherHomeHosuAndUserCustomer_Id(String otherHomeId, String dong, String hosu, String uuid);
}
