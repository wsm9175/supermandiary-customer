package com.lodong.spring.supermandiarycustomer.controller;

import com.lodong.spring.supermandiarycustomer.dto.constructor.ConstructorDTO;
import com.lodong.spring.supermandiarycustomer.dto.main.MainInfoDTO;
import com.lodong.spring.supermandiarycustomer.dto.main.MyAddressDTO;
import com.lodong.spring.supermandiarycustomer.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiarycustomer.responseentity.StatusEnum;
import com.lodong.spring.supermandiarycustomer.service.MainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.lodong.spring.supermandiarycustomer.util.MakeResponseEntity.getResponseMessage;

@Slf4j
@RestController
@RequestMapping("rest/v1/main/customer")

public class MainController {
    private final MainService mainService;
    private final JwtTokenProvider jwtTokenProvider;

    public MainController(MainService mainService, JwtTokenProvider jwtTokenProvider) {
        this.mainService = mainService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/address")
    public ResponseEntity<?> getAddress(@RequestHeader(name = "Authorization") String token){
        try {
            List<MyAddressDTO> myAddressDTOS = mainService.getMyAddressList(getMyUuId(token));
            return getResponseMessage(StatusEnum.OK, "나의 주소 목록", myAddressDTOS);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.OK, nullPointerException.getMessage());
        }
    }

    @GetMapping("/info")
    public ResponseEntity<?> getMainInfo(@RequestHeader(name = "Authorization") String token){
        try{
            MainInfoDTO mainInfoDTO =  mainService.getMainInfo(getMyUuId(token));
            return getResponseMessage(StatusEnum.OK, "메인화면 정보", mainInfoDTO);
        }catch (NullPointerException nullPointerException){
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    @GetMapping("/near")
    public ResponseEntity<?> getNear(@RequestHeader(name = "Authorization") String token,String addressId){
        try{
            List<ConstructorDTO> nearConstructorList =  mainService.getNearConstructor(getMyUuId(token), addressId);
            return getResponseMessage(StatusEnum.OK, "메인화면 정보", nearConstructorList);
        }catch (NullPointerException nullPointerException){
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    @GetMapping("/find")
    public ResponseEntity<?> findConstructor(String keyword){
        List<ConstructorDTO> constructorDTOList = mainService.findConstructor(keyword);
        return getResponseMessage(StatusEnum.OK, "시공사 검색 결과", constructorDTOList);
    }

    private String getMyUuId(String token) throws NullPointerException {
        return jwtTokenProvider.getUserUuid(token.substring(7));
    }
}
