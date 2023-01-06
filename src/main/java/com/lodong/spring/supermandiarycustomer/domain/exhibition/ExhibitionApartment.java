package com.lodong.spring.supermandiarycustomer.domain.exhibition;

import com.lodong.spring.supermandiarycustomer.domain.apart.Apartment;
import jakarta.persistence.*;
import lombok.*;

@NamedEntityGraph(name = "exhibition-apartment-get-all", attributeNodes = {
        @NamedAttributeNode(value = "exhibition"),
        @NamedAttributeNode(value = "apartment", subgraph = "siggAreas")
}, subgraphs = @NamedSubgraph(name = "siggAreas", attributeNodes = {
    @NamedAttributeNode("siggAreas")
}))

@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionApartment {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;
    @Column
    private String apartmentName;
}
