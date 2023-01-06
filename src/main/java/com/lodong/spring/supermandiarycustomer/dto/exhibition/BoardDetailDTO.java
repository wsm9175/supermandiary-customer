package com.lodong.spring.supermandiarycustomer.dto.exhibition;

import com.lodong.spring.supermandiarycustomer.domain.exhibition.ExhibitionComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDetailDTO {
    private String constructorName;
    private String videoLink;
    private String tag;
    private List<String> priceTableFileName;
    private List<ExhibitionCommentDTO> exhibitionCommentList;
}
