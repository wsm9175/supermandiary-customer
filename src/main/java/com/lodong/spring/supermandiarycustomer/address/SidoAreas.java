package com.lodong.spring.supermandiarycustomer.address;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
@Table(name = "sido_areas")
public class SidoAreas {
    @Id
    private int code;
    @Column(nullable = false)
    private String name;

    @PrePersist
    public void prePersist(){

    }
}
