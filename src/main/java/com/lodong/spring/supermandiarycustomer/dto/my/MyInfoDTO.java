package com.lodong.spring.supermandiarycustomer.dto.my;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyInfoDTO {
    private String name;
    private List<MyAddressListDTO> addressList;
    private String phoneNumber;
}
