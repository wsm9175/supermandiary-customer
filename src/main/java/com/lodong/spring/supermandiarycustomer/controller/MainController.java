package com.lodong.spring.supermandiarycustomer.controller;

import com.lodong.spring.supermandiarycustomer.dto.auth.FcmDTO;
import com.lodong.spring.supermandiarycustomer.dto.constructor.ConstructorDTO;
import com.lodong.spring.supermandiarycustomer.dto.main.AlarmDTO;
import com.lodong.spring.supermandiarycustomer.dto.main.MainInfoDTO;
import com.lodong.spring.supermandiarycustomer.dto.main.MyAddressDTO;
import com.lodong.spring.supermandiarycustomer.dto.main.ReadAllAlarmDTO;
import com.lodong.spring.supermandiarycustomer.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiarycustomer.responseentity.StatusEnum;
import com.lodong.spring.supermandiarycustomer.service.MainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/alarm")
    public ResponseEntity<?> getAlarm(@RequestHeader(name = "Authorization") String token){
        List<AlarmDTO> alarmDTOS = mainService.getAlarmList(getMyUuId(token));
        return getResponseMessage(StatusEnum.OK, "알림 목록", alarmDTOS);
    }

    @PatchMapping("/alarm/read")
    public ResponseEntity<?> readAlarm(@RequestHeader(name = "Authorization") String token, String alarmId){
        try{
            mainService.readAlarm(alarmId);
            return getResponseMessage(StatusEnum.OK, "알림 읽음 처리", null);
        }catch (NullPointerException nullPointerException){
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    @PatchMapping("/alarm/read-all")
    public ResponseEntity<?> readAllAlarm(@RequestBody ReadAllAlarmDTO readAllAlarmDTO){
        mainService.readAllAlarm(readAllAlarmDTO);
        return getResponseMessage(StatusEnum.OK, "모든 알림 읽음 처리", null);
    }

    @PatchMapping("/fcm")
    public ResponseEntity<?> registerFCMToken(@RequestHeader(name = "Authorization") String token,@RequestBody FcmDTO fcmDTO) {
        log.info("fcm received by {}", fcmDTO.getFcmId());
        mainService.registerFCMToken(getMyUuId(token), fcmDTO.getFcmId());

        return ResponseEntity.ok().build();
    }

    private String getMyUuId(String token) throws NullPointerException {
        return jwtTokenProvider.getUserUuid(token.substring(7));
    }
}
