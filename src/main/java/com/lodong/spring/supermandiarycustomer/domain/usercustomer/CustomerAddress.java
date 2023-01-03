package com.lodong.spring.supermandiarycustomer.domain.usercustomer;

import com.lodong.spring.supermandiarycustomer.domain.apart.OtherHome;
import com.lodong.spring.supermandiarycustomer.domain.apart.Apartment;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CustomerAddress {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "customer_id")
    private UserCustomer userCustomer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;
    private String apartmentDong;
    private String apartmentHosu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "other_home_id")
    private OtherHome otherHome;
    private String otherHomeDong;
    private String otherHomeHosu;

}
