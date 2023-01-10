package com.lodong.spring.supermandiarycustomer.domain.usercustomer;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserCustomerAlarm {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_customer_id")
    private UserCustomer userCustomer;
    @Column(nullable = false)
    private String kind;
    @Column(nullable = false)
    private String content;
    @Column(nullable = true)
    private String detail;
    @Column(nullable = false)
    private LocalDateTime createAt;
    @Column(nullable = false)
    private boolean isRead;
}
