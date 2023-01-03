package com.lodong.spring.supermandiarycustomer.repository;

import com.lodong.spring.supermandiarycustomer.address.SiggAreas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SiggAreasRepository extends JpaRepository<SiggAreas, Integer> {
    Optional<SiggAreas> findByName(String name);
}
