package com.lodong.spring.supermandiarycustomer.dto.auth;

import lombok.Data;

@Data
public class LoginDTO {
    private String phoneNumber;
    private String pw;
}
