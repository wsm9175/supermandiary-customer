package com.lodong.spring.supermandiarycustomer.dto.constructor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewPageDTO {
    private long totalCount;
    private List<String> fileNames;
    private List<ReviewDetailDTO> reviewInfos;
}
