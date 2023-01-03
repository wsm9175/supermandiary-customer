package com.lodong.spring.supermandiarycustomer.domain.estimate;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EstimateDetail {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimate_id")
    private Estimate estimate;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private int count;
    @Column(nullable = false)
    private int price;

}
