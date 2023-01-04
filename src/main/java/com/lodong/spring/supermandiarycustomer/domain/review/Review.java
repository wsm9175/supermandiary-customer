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
import java.util.Set;

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
                @NamedAttributeNode(value = "reviewImageFileList", subgraph = "image"),
                @NamedAttributeNode("reviewLikeList")
        }, subgraphs = {
                @NamedSubgraph(name = "image", attributeNodes = {
                        @NamedAttributeNode("fileList")
                })
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
    @OneToOne(fetch = FetchType.LAZY)
    private ReviewComment reviewComment;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "review")
    private Set<ReviewImageFile> reviewImageFileList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "review")
    private Set<ReviewLike> reviewLikeList;
}
