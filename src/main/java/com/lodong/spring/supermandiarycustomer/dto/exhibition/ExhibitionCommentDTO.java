package com.lodong.spring.supermandiarycustomer.dto.exhibition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionCommentDTO {
    private String id;
    private String commentGroupId;
    private int sequence;
    private String comment;
    private String constructorName;
    private String userCustomerName;
    private boolean isIsMine;
    private LocalDateTime createAt;
    private boolean hasCommentGroup;
}
