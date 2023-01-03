package com.lodong.spring.supermandiarycustomer.domain.usercustomer;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CustomerPhoneNumber {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "customer_id")
    private UserCustomer userCustomer;

    private String phoneNumber;
}
