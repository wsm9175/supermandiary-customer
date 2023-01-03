package com.lodong.spring.supermandiarycustomer.domain.constructor;

import com.lodong.spring.supermandiarycustomer.address.SiggAreas;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor

public class ConstructorFaq {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constructor_id")
    private Constructor constructor;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private String question;
    @Column(nullable = false)
    private String answer;
}
