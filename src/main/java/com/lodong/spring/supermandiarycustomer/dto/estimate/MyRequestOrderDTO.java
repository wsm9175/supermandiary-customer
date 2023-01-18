package com.lodong.spring.supermandiarycustomer.dto.estimate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyRequestOrderDTO {
    private String requestOrderId;
    private String constructorName;
    private String homeName;
    private String homeDong;
    private String homeHosu;
    private String type;
    private String productName;
    private String status;
    private LocalDateTime localDateTime;
}
