package com.lodong.spring.supermandiarycustomer.dto.constructor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConstructorDetailDTO {
    private String constructorName;
    private List<String> possibleArea;
    private String introduce;
    private List<String> priceFileName;
    private List<ReviewDTO> reviewList;
    private List<String> questionList;
}
