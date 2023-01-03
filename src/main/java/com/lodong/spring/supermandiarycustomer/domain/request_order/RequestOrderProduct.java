package com.lodong.spring.supermandiarycustomer.domain.request_order;

import com.lodong.spring.supermandiarycustomer.domain.constructor.ConstructorProduct;
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


public class RequestOrderProduct {
    @Id
    private int id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_order_id")
    private RequestOrder requestOrder;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ConstructorProduct constructorProduct;
}
