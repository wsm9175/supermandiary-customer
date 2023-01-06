package com.lodong.spring.supermandiarycustomer.dto.exhibition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionDTO {
    private String id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isIsOfflineOn;
}
