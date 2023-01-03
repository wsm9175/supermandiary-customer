package com.lodong.spring.supermandiarycustomer.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyWorkDTO {
    private String workId;
    private String constructorId;
    private boolean isIsPayComplete;
    private String constructorName;
    private String homeName;
    private String homeDong;
    private String homeHosu;
    private String type;
    private String productName;
    private String nowWorkLevel;
}
