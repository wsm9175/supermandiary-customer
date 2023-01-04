package com.lodong.spring.supermandiarycustomer.dto.constructor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewPagingDTO {
    private int totalCount;
    private List<ReviewDetailDTO> reviewInfos;
}
