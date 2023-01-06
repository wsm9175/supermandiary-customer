package com.lodong.spring.supermandiarycustomer.dto.exhibition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionDetailDTO {
    private String exhibitionName;
    private String userName;
    private List<CategoryDTO> categoryList;
    private List<BoardDTO> boardList;
}
