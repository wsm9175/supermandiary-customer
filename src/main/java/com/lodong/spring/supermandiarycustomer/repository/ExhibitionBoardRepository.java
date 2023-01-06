package com.lodong.spring.supermandiarycustomer.repository;

import com.lodong.spring.supermandiarycustomer.domain.exhibition.ExhibitionBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExhibitionBoardRepository extends JpaRepository<ExhibitionBoard, String> {
    public Page<ExhibitionBoard> findByExhibition_Id(String id, Pageable pageable);
}
