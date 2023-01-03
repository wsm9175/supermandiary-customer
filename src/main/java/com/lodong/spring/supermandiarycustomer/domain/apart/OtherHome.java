package com.lodong.spring.supermandiarycustomer.domain.apart;

import com.lodong.spring.supermandiarycustomer.address.SiggAreas;
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

public class OtherHome {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sigg_code")
    private SiggAreas siggAreas;
    @Column(nullable = false)
    private int bjd_code;
    @Column(nullable = false)
    private String name;
}
