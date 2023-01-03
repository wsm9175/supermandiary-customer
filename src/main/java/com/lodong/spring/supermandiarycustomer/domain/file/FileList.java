package com.lodong.spring.supermandiarycustomer.domain.file;

import com.lodong.spring.supermandiarycustomer.domain.constructor.ConstructorImageFile;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
@Embeddable
public class FileList {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String extension;

    @Column(nullable = false)
    private String storage;

    @Column(nullable = false)
    private String createAt;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "fileList", cascade = CascadeType.ALL)
    private WorkFile workFile;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "fileList", cascade = CascadeType.ALL)
    private ReviewImageFile reviewImageFile;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "fileList", cascade = CascadeType.ALL)
    private ConstructorImageFile constructorImageFile;

    @PrePersist
    public void prePersist() {

    }
}
