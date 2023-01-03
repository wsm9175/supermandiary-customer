package com.lodong.spring.supermandiarycustomer.dto.constructor;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ConstructorDTO {
    private String constructorId;
    private String constructorName;
    private String constructorIntroduction;
    private String logoFileName;

}
