package com.lodong.spring.supermandiarycustomer.domain.review;

import com.lodong.spring.supermandiarycustomer.domain.constructor.Constructor;
import com.lodong.spring.supermandiarycustomer.domain.file.ReviewImageFile;
import com.lodong.spring.supermandiarycustomer.domain.usercustomer.UserCustomer;
import com.lodong.spring.supermandiarycustomer.domain.working.Working;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@NamedEntityGraphs({
        @NamedEntityGraph(name = "get-review-with-all", attributeNodes = {
                @NamedAttributeNode("constructor"),
                @NamedAttributeNode("working"),
                @NamedAttributeNode("customer"),
                @NamedAttributeNode("reviewComment"),
                @NamedAttributeNode("reviewImageFileList")
        })
})

public class Review {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constructor_id")
    private Constructor constructor;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_id")
    private Working working;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private UserCustomer customer;
    @Column(nullable = false)
    private String customerName;
    @Column(nullable = false)
    private LocalDate constructorDate;
    @Column(nullable = false)
    private String contents;
    @Column(nullable = false)
    private boolean isSatisfaction;
    @Column(nullable = false)
    private LocalDate createAt;
    @Column(nullable = false)
    private int likeCount;
    @OneToOne(fetch = FetchType.LAZY)
    private ReviewComment reviewComment;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "review")
    private List<ReviewImageFile> reviewImageFileList;
}
