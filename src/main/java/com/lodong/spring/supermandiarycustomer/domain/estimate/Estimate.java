package com.lodong.spring.supermandiarycustomer.domain.estimate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lodong.spring.supermandiarycustomer.domain.apart.Apartment;
import com.lodong.spring.supermandiarycustomer.domain.apart.OtherHome;
import com.lodong.spring.supermandiarycustomer.domain.constructor.Constructor;
import com.lodong.spring.supermandiarycustomer.domain.constructor.ConstructorProduct;
import com.lodong.spring.supermandiarycustomer.domain.request_order.RequestOrder;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j @Entity
@Builder @Getter @Setter
@AllArgsConstructor @NoArgsConstructor

@NamedEntityGraph(name = "get-with-requestorder-product", attributeNodes = {
        @NamedAttributeNode(value = "requestOrder", subgraph = "customer"),
        @NamedAttributeNode(value = "constructorProduct")
        },
        subgraphs = @NamedSubgraph(name = "customer", attributeNodes = {
                @NamedAttributeNode("customer")
        })
)

public class Estimate {
    @Id
    private String id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constructor_id")
    private Constructor constructor;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ConstructorProduct constructorProduct;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "request_order_id")
    private RequestOrder requestOrder;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_code")
    private Apartment apartment;
    @Column(nullable = true)
    private String apartment_dong;
    @Column(nullable = true)
    private String apartment_hosu;
    @Column(nullable = true)
    private String apartment_type;
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "other_home_id")
    private OtherHome otherHome;
    @Column(nullable = true)
    private String otherHomeDong;
    @Column(nullable = true)
    private String otherHomeHosu;
    @Column(nullable = true)
    private String otherHomeType;
    @Column(nullable = true)
    private String note;
    @Column(nullable = true)
    private String name;
    @Column(nullable = true)
    private String phoneNumber;
    @Column(nullable = true)
    private String remark;
    @Column(nullable = true)
    private boolean isCashReceipt;
    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    private boolean isVat;
    @Column(nullable = false)
    private String status;

    @JsonIgnore
    @OneToMany(mappedBy = "estimate", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EstimateDetail> estimateDetails = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "estimate", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Discount> discountList = new ArrayList<>();

}
