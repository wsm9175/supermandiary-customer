package com.lodong.spring.supermandiarycustomer.dto.exhibition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionCommentAlterDTO {
    private String commentId;
    private String contents;
}
