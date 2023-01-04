package com.lodong.spring.supermandiarycustomer.domain.review;


import com.lodong.spring.supermandiarycustomer.domain.usercustomer.UserCustomer;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

public class ReviewLike {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserCustomer userCustomer;
}
