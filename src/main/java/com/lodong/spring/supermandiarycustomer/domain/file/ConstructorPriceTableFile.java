package com.lodong.spring.supermandiarycustomer.domain.file;

import com.lodong.spring.supermandiarycustomer.address.SiggAreas;
import com.lodong.spring.supermandiarycustomer.domain.constructor.Constructor;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
public class ConstructorPriceTableFile {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constructorId")
    private Constructor constructor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fileId")
    private FileList fileList;
}
