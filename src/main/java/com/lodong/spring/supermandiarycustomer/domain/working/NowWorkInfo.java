package com.lodong.spring.supermandiarycustomer.domain.working;

import jakarta.persistence.*;
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

@NamedEntityGraph(name = "get-with-workDetail", attributeNodes = {
        @NamedAttributeNode("workDetail")
})
public class NowWorkInfo {
    @Id
    private String id;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "work_id", nullable = false)
    private Working working;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "work_detail_id")
    private WorkDetail workDetail;

}
