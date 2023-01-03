package com.lodong.spring.supermandiarycustomer.domain.apart;

import com.lodong.spring.supermandiarycustomer.address.SiggAreas;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
public class Apartment {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sigg_code")
    private SiggAreas siggAreas;
    @Column(nullable = false)
    private long bjdCode;
    @Column(nullable = false)
    private String name;

    @PrePersist
    public void prePersist() {

    }

}
