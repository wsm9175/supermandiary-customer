package com.lodong.spring.supermandiarycustomer.domain.constructor;

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

public class ConstructorAlarm {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constructor_id")
    private Constructor constructor;
    @Column(nullable = false)
    private String kind;
    @Column(nullable = true)
    private String detail;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private LocalDateTime createAt;
    @Column(nullable = false)
    private boolean isRead;
}
