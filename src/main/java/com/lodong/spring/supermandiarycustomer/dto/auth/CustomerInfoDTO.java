package com.lodong.spring.supermandiarycustomer.dto.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CustomerInfoDTO {
    private String name;
    private String sex;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birth;
    private String phoneNumber;
    private List<String> subPhoneNumberList;
    private String pw;
    private String email;
    private List<CustomerAddressDTO> customerAddressList;
    private boolean isInterestInInterior;
    private List<String> interestProduct;
    private boolean isCertification;
    private boolean isAgreeTerm;
}
