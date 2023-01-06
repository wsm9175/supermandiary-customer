package com.lodong.spring.supermandiarycustomer.domain.exhibition;

import com.lodong.spring.supermandiarycustomer.domain.constructor.UserConstructor;
import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionParticipateConstructor {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constructor_id")
    private UserConstructor userConstructor;
    @Column(nullable = false)
    private String constructorName;
    @Column(nullable = false)
    private boolean isAnswer;
    @Column(nullable = false)
    private boolean isPay;
    @Column
    private String constructorIntroduce;
}
