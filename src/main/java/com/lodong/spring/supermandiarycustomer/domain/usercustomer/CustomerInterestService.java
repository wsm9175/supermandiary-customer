package com.lodong.spring.supermandiarycustomer.domain.usercustomer;

import com.lodong.spring.supermandiarycustomer.domain.constructor.Product;
import jakarta.persistence.*;
import lombok.*;
@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInterestService {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private UserCustomer userCustomer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
