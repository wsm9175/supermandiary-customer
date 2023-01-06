package com.lodong.spring.supermandiarycustomer.domain.exhibition;

import com.lodong.spring.supermandiarycustomer.domain.constructor.Constructor;
import com.lodong.spring.supermandiarycustomer.domain.usercustomer.UserCustomer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NamedEntityGraph(
        name = "get-with-all-exhibiton-comment", attributeNodes = {
        @NamedAttributeNode(value = "exhibitionBoard"),
        @NamedAttributeNode(value = "constructor"),
        @NamedAttributeNode(value = "userCustomer"),
})

@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionComment {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private ExhibitionBoard exhibitionBoard;
    @Column
    private String commentGroupId;
    @Column
    private int sequence;
    @Column
    private String comment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constructor_id")
    private Constructor constructor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_customer_id")
    private UserCustomer userCustomer;
    @Column(nullable = false)
    private LocalDateTime createAt;
    @Column(nullable = false)
    private boolean hasCommentGroup;
}
