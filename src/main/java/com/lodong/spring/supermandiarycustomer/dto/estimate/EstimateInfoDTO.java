package com.lodong.spring.supermandiarycustomer.dto.estimate;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data @AllArgsConstructor
public class EstimateInfoDTO {
    private String id;
    private List<EstimateDetailDto> estimateDetailList;
    private List<DiscountDto> discountDtoList;
    private int totalPrice;
    private boolean isVat;
    private String constructorNote;
}
