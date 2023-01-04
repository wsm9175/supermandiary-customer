package com.lodong.spring.supermandiarycustomer.dto.constructor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ReviewDetailDTO {
    private String reviewId;
    private String writerId;
    private String writerName;
    private LocalDate createAt;
    private int likeCount;
    private boolean meCheckLike;
    private List<String> likeUserIdList;
    private List<String> fileNameList;
    private String productName;
    private String contents;
    private ReviewCommentDTO reviewComment;
}
