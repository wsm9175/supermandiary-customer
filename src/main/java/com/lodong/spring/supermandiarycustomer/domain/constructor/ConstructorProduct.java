package com.lodong.spring.supermandiarycustomer.domain.constructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ConstructorProduct {
    @Id
    private String id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constructor_id")
    private Constructor constructor;
    @Column(nullable = false)
    private String name;
    private String introduction;
    private int price;

    @JsonIgnore
    @OneToMany(mappedBy = "constructorProduct", fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = ConstructorProductWorkList.class)
    private List<ConstructorProductWorkList> constructorProductWorkLists = new ArrayList<>();
}
