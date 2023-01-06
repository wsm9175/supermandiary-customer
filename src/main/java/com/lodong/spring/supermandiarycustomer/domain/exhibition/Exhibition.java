package com.lodong.spring.supermandiarycustomer.domain.exhibition;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Exhibition {
    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalDate createDate;
    @Column(nullable = false)
    private LocalDateTime startDateTime;
    @Column(nullable = false)
    private LocalDateTime endDateTime;
    @Column(nullable = false)
    private boolean isOfflineOn;
    @Column
    private LocalDateTime offlineStartDateTime;
    private LocalDateTime offlineEndDateTime;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "exhibition")
    private List<ExhibitionBoard> exhibitionBoardList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "exhibition")
    private List<ExhibitionCategory> exhibitionCategoryList;
}
