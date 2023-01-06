package com.lodong.spring.supermandiarycustomer.repository;

import com.lodong.spring.supermandiarycustomer.domain.exhibition.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExhibitionRepository extends JpaRepository<Exhibition, String> {

}
