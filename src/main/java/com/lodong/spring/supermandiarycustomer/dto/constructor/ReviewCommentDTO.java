package com.lodong.spring.supermandiarycustomer.dto.constructor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCommentDTO {
    private String id;
    private String constructorName;
    private LocalDate creatAt;
    private String contents;
}
