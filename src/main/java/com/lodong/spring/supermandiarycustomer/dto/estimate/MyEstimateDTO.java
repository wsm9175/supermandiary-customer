package com.lodong.spring.supermandiarycustomer.dto.estimate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyEstimateDTO {
    private String estimateId;
    private String constructorName;
    private String homeName;
    private String homeDong;
    private String homeHosu;
    private String type;
    private String productName;
    private String status;
}
