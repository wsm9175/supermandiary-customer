package com.lodong.spring.supermandiarycustomer.domain.constructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Product {
    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    @Column
    private String detail;
}
