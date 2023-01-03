package com.lodong.spring.supermandiarycustomer.dto.constructor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewDTO {
    private String reviewId;
    private List<String> fileName;
    private String writerId;
    private String writerName;
    private boolean isSatisFaction;
    private String contents;
}
