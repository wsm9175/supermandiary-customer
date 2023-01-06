package com.lodong.spring.supermandiarycustomer.dto.exhibition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    private String id;
    private List<String> categoryIdList;
    private String videoLink;
    private String tag;
}
