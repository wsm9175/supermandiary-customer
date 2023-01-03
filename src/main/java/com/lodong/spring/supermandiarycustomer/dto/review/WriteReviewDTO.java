package com.lodong.spring.supermandiarycustomer.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class WriteReviewDTO {
    private String workId;
    private String constructorId;
    private boolean isSatisfaction;
    private String content;
}
