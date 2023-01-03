package com.lodong.spring.supermandiarycustomer.domain.review;

import com.lodong.spring.supermandiarycustomer.domain.constructor.Constructor;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;

@Slf4j
@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewComment {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constructor_id")
    private Constructor constructor;
    @Column(nullable = false)
    private String constructorName;
    @Column(nullable = false)
    private String contents;
    @Column(nullable = false)
    private LocalDate createAt;
}
