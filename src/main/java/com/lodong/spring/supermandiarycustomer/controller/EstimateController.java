package com.lodong.spring.supermandiarycustomer.controller;
import com.lodong.spring.supermandiarycustomer.dto.estimate.AgreeDTO;
import com.lodong.spring.supermandiarycustomer.dto.estimate.EstimateInfoDto;
import com.lodong.spring.supermandiarycustomer.dto.estimate.MyEstimateDTO;
import com.lodong.spring.supermandiarycustomer.dto.estimate.RejectEstimateDTO;
import com.lodong.spring.supermandiarycustomer.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiarycustomer.responseentity.StatusEnum;
import com.lodong.spring.supermandiarycustomer.service.EstimateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lodong.spring.supermandiarycustomer.util.MakeResponseEntity.getResponseMessage;

@Slf4j
@RestController
@RequestMapping("rest/v1/estimate/customer")

public class EstimateController {
    private final JwtTokenProvider jwtTokenProvider;
    private final EstimateService estimateService;

    public EstimateController(JwtTokenProvider jwtTokenProvider, EstimateService estimateService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.estimateService = estimateService;
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyEstimateList(@RequestHeader(name = "Authorization") String token) {
        String uuid = getMyUuId(token);
        try {
            List<MyEstimateDTO> estimateDTOList = estimateService.getMyEstimateList(uuid);
            return getResponseMessage(StatusEnum.OK, "회원 견적서 목록", estimateDTOList);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    @GetMapping("/show")
    public ResponseEntity<?> getEstimateDetail(String estimateId) {
        try {
            EstimateInfoDto estimateInfoDto = estimateService.getEstimate(estimateId);
            return getResponseMessage(StatusEnum.OK, "견적서 상세 정보", estimateInfoDto);
        } catch (NullPointerException nullPointerException) {
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    @PatchMapping("/reject")
    public ResponseEntity<?> rejectEstimate(@RequestBody RejectEstimateDTO rejectEstimate) {
        try {
            estimateService.rejectEstimate(rejectEstimate);
            return getResponseMessage(StatusEnum.OK, "견적서 반려 성공", null);
        } catch (NullPointerException nullPointerException) {
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage(), null);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteOrder(String estimateId) {
        estimateService.deleteEstimate(estimateId);
        return getResponseMessage(StatusEnum.OK, "주문 취소 성공", null);
    }

    @PostMapping("/agree")
    public ResponseEntity<?> agreeEstimate(@RequestBody AgreeDTO agreeDTO) {
        try {
            estimateService.agreeEstimate(agreeDTO);
            return getResponseMessage(StatusEnum.OK, "견적서 수락 성공", null);
        }catch (NullPointerException nullPointerException){
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage(), null);
        }
    }
    private String getMyUuId(String token) throws NullPointerException {
        return jwtTokenProvider.getUserUuid(token.substring(7));
    }
}
