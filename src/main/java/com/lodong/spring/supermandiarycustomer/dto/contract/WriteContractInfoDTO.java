package com.lodong.spring.supermandiarycustomer.dto.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WriteContractInfoDTO {
    private String customerName;
    private String mainPhoneNumber;
    private List<String> subPhoneNumberList;
    private List<MyAddressDTO> myAddressList;
    private List<ConstructorProductDTO> constructorProductList;
}
