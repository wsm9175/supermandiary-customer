package com.lodong.spring.supermandiarycustomer.controller;

import com.lodong.spring.supermandiarycustomer.dto.constructor.ConstructorDTO;
import com.lodong.spring.supermandiarycustomer.dto.constructor.ConstructorDetailDTO;
import com.lodong.spring.supermandiarycustomer.dto.constructor.ReviewPageDTO;
import com.lodong.spring.supermandiarycustomer.dto.constructor.ReviewPagingDTO;
import com.lodong.spring.supermandiarycustomer.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiarycustomer.responseentity.StatusEnum;
import com.lodong.spring.supermandiarycustomer.service.ConstructorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lodong.spring.supermandiarycustomer.util.MakeResponseEntity.getResponseMessage;

@Slf4j
@RestController
@RequestMapping("rest/v1/constructor/customer")
public class ConstructorController {
    private ConstructorService constructorService;
    private final JwtTokenProvider jwtTokenProvider;

    public ConstructorController(ConstructorService constructorService, JwtTokenProvider jwtTokenProvider) {
        this.constructorService = constructorService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/constructor")
    public ResponseEntity<?> getConstructorList(@RequestHeader(name = "Authorization") String token) {
        try {
            List<ConstructorDTO> constructorDTOS = constructorService.getConstructor(getMyUuId(token));
            return getResponseMessage(StatusEnum.OK, "주위 시공사 목록", constructorDTOS);
        } catch (NullPointerException nullPointerException) {
            return getResponseMessage(StatusEnum.OK, nullPointerException.getMessage());
        }
    }

    @GetMapping("/constructor-detail")
    public ResponseEntity<?> getConstructorDetail(String constructorId) {
        try {
            ConstructorDetailDTO constructorDetailDTO = constructorService.getConstructorDetail(constructorId);
            return getResponseMessage(StatusEnum.OK, "시공사 상세 정보", constructorDetailDTO);
        } catch (NullPointerException nullPointerException) {
            return getResponseMessage(StatusEnum.OK, nullPointerException.getMessage());
        }
    }

    @GetMapping("/review/init")
    public ResponseEntity<?> reviewPageInit(@RequestHeader(name = "Authorization") String token,String constructorId) {
        try {
            ReviewPageDTO reviewPageDTO = constructorService.getReviewPageInfo(getMyUuId(token), constructorId);
            return getResponseMessage(StatusEnum.OK, "해당 시공사 리뷰 리스트", reviewPageDTO);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    @GetMapping("/review")
    public ResponseEntity<?> reviewPage(@RequestHeader(name = "Authorization") String token,String constructorId, int page) {
        try {
            ReviewPagingDTO reviewPageDTO = constructorService.getReviewPage(getMyUuId(token),constructorId, page);
            return getResponseMessage(StatusEnum.OK, "해당 시공사 리뷰 리스트", reviewPageDTO);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    @PatchMapping("/review/like")
    public ResponseEntity<?> reviewLike(@RequestHeader(name = "Authorization") String token, String reviewId){
        try{
            constructorService.reviewLike(getMyUuId(token), reviewId);
            return getResponseMessage(StatusEnum.OK, "좋아요 성공", null);
        }catch (NullPointerException nullPointerException){
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }catch (DataIntegrityViolationException dataIntegrityViolationException){
            dataIntegrityViolationException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, "해당 회원은 이미 좋아요를 한 회원입니다.");
        }
    }

    @PatchMapping("/review/unlike")
    public ResponseEntity<?> reviewUnLike(@RequestHeader(name = "Authorization") String token, String reviewId){
        try{
            constructorService.reviewUnLike(getMyUuId(token), reviewId);
            return getResponseMessage(StatusEnum.OK, "좋아요 취소 성공", null);
        }catch (NullPointerException nullPointerException){
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }


    private String getMyUuId(String token) throws NullPointerException {
        return jwtTokenProvider.getUserUuid(token.substring(7));
    }
}
