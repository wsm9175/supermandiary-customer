package com.lodong.spring.supermandiarycustomer.dto.constructor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MyReviewDTO {
    private String reviewId;
    private List<String> fileName;
    private String writerId;
    private String writerName;
    private boolean isSatisFaction;
    private String contents;
    private LocalDate writeDate;
    private int like;
    private String productName;
}
