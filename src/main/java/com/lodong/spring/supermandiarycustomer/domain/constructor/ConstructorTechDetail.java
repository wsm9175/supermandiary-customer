package com.lodong.spring.supermandiarycustomer.domain.constructor;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.*;

@Entity
@Getter @Setter @ToString
@Builder
@AllArgsConstructor @NoArgsConstructor
public class ConstructorTechDetail {
    @Id
    private String id;
    @Column(nullable = false)
    private String constructorId;
    @Column(nullable = false)
    private String name;

    @PrePersist
    public void prePersist() {

    }

}
