package com.lodong.spring.supermandiarycustomer.dto.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyAddressDTO {
    private String addressId;
    private String homeName;
    private String dong;
    private String hosu;
}
