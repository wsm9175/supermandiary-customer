package com.lodong.spring.supermandiarycustomer.domain.exhibition;

import jakarta.persistence.*;
import lombok.*;


@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ExhibitionCategoryIndex {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private ExhibitionBoard exhibitionBoard;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ExhibitionCategory category;
}

