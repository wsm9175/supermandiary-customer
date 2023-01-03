package com.lodong.spring.supermandiarycustomer.domain.constructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;


@Entity
@Getter @Setter
@ToString @Builder @AllArgsConstructor @NoArgsConstructor
@BatchSize(size = 10)
public class ConstructorProductWorkList {
    @Id
    private String id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ConstructorProduct constructorProduct;
    @Column(nullable = false)
    private int sequence;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private boolean isFileIn;

    @PrePersist
    public void prePersist() {

    }
}
