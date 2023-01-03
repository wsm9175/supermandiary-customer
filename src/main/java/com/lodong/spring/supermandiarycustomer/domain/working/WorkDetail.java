package com.lodong.spring.supermandiarycustomer.domain.working;

import com.lodong.spring.supermandiarycustomer.domain.constructor.UserConstructor;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@NamedEntityGraphs(
        @NamedEntityGraph(name = "workDetail-with-all", attributeNodes = {
                @NamedAttributeNode(value = "working", subgraph = "apartment"),
                @NamedAttributeNode("userConstructor"),
        },
                subgraphs = {
                        @NamedSubgraph(name = "apartment", attributeNodes = {
                                @NamedAttributeNode("apartment"),
                                @NamedAttributeNode("otherHome"),
                                @NamedAttributeNode("constructorProduct"),
                                @NamedAttributeNode("nowWorkInfo"),
                        })
                }
        )
)
public class WorkDetail {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "work_id")
    private Working working;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_constructor_id")
    private UserConstructor userConstructor;
    @Column
    private String note;
    @Column
    private LocalDate estimateWorkDate;
    @Column
    private LocalTime estimateWorkTime;
    @Column
    private LocalDate actualWorkDate;
    @Column
    private LocalTime actualWorkTime;
    @Column
    private boolean isComplete;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int sequence;
    @Column(nullable = false)
    private boolean isFileIn;
}
