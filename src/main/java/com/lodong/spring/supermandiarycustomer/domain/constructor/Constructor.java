package com.lodong.spring.supermandiarycustomer.domain.constructor;

import com.lodong.spring.supermandiarycustomer.domain.file.ConstructorPriceTableFile;
import com.lodong.spring.supermandiarycustomer.domain.review.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

@NamedEntityGraphs({
        @NamedEntityGraph(name = "get-all-data", attributeNodes = {
                @NamedAttributeNode(value = "constructorProducts"),
                @NamedAttributeNode(value = "affiliatedInfoList"),
                @NamedAttributeNode(value = "constructorWorkAreas")
        }),
        @NamedEntityGraph(name = "get-constructor-detail", attributeNodes = {
                @NamedAttributeNode(value = "constructorWorkAreas", subgraph = "sigg"),
                @NamedAttributeNode(value = "constructorPriceTableFiles", subgraph = "file_list"),
                @NamedAttributeNode(value = "reviewList", subgraph = "review"),
                @NamedAttributeNode(value = "constructorFaqlist")
        },
        subgraphs = {
                @NamedSubgraph(name = "sigg", attributeNodes = {
                        @NamedAttributeNode(value = "siggAreas")
                }),
                @NamedSubgraph(name = "file_list", attributeNodes = {
                        @NamedAttributeNode(value = "fileList")
                }),
                @NamedSubgraph(name = "review", attributeNodes = {
                        @NamedAttributeNode(value = "reviewImageFileList")
                })
        })
})


public class Constructor {
    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    @Column
    private String introduction;
    @Column(nullable = false)
    private boolean payActivation;
    @Column(nullable = false)
    private boolean orderManage;
    @Column(nullable = false)
    private boolean payManage;
    @Column(nullable = false)
    private boolean webAdminActive;

    @Column
    private String callingNumber;
    @Column(nullable = false)
    private boolean isCertificatePhoneNumber;
    @Column
    private String bank;
    private String bankAccount;
    private String payTemplate;
    private String orderMethod;
    private String placeOrder;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "constructor")
    private List<ConstructorProduct> constructorProducts = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "constructor")
    private List<AffiliatedInfo> affiliatedInfoList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "constructor")
    private List<ConstructorWorkArea> constructorWorkAreas = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "constructor")
    private List<ConstructorProduct> constructorProductList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "constructor")
    private List<ConstructorPriceTableFile> constructorPriceTableFiles;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "constructor")
    private List<Review> reviewList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "constructor")
    private List<ConstructorFaq> constructorFaqlist;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "constructor")
    private ConstructorImageFile constructorImageFile;

    @PrePersist
    public void prePersist() {

    }

    public Constructor(String id, String name, boolean payActivation, boolean orderManage, boolean payManage, boolean webAdminActive) {
        this.id = id;
        this.name = name;
        this.payActivation = payActivation;
        this.orderManage = orderManage;
        this.payManage = payManage;
        this.webAdminActive = webAdminActive;
    }

    public static Constructor getPublicProfile(Constructor constructor) {
        return new Constructor(constructor.getId(), constructor.getName(), constructor.isPayActivation(), constructor.isOrderManage(), constructor.isPayManage(), constructor.isWebAdminActive());
    }
}
