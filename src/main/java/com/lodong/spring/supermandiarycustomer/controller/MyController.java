package com.lodong.spring.supermandiarycustomer.controller;

import com.lodong.spring.supermandiarycustomer.dto.my.MyInfoDTO;
import com.lodong.spring.supermandiarycustomer.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiarycustomer.responseentity.StatusEnum;
import com.lodong.spring.supermandiarycustomer.service.MyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.lodong.spring.supermandiarycustomer.util.MakeResponseEntity.getResponseMessage;

@Slf4j
@RestController
@RequestMapping("rest/v1/my/customer")

public class MyController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MyService myService;

    public MyController(JwtTokenProvider jwtTokenProvider, MyService myService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.myService = myService;
    }

    @GetMapping("/info")
    public ResponseEntity<?> getMyInfo(@RequestHeader(name = "Authorization") String token){
        try{
            MyInfoDTO myInfoDTO = myService.getMyInfo(getMyUuId(token));
            return getResponseMessage(StatusEnum.OK, "내정보", myInfoDTO);
        }catch (NullPointerException nullPointerException){
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    private String getMyUuId(String token) throws NullPointerException {
        return jwtTokenProvider.getUserUuid(token.substring(7));
    }
}
