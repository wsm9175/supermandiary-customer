package com.lodong.spring.supermandiarycustomer.controller;

import com.lodong.spring.supermandiarycustomer.dto.constructor.MyReviewDTO;
import com.lodong.spring.supermandiarycustomer.dto.review.MyWorkDTO;
import com.lodong.spring.supermandiarycustomer.dto.review.WriteReviewDTO;
import com.lodong.spring.supermandiarycustomer.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiarycustomer.responseentity.StatusEnum;
import com.lodong.spring.supermandiarycustomer.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.lodong.spring.supermandiarycustomer.util.MakeResponseEntity.getResponseMessage;


@Slf4j
@RestController
@RequestMapping("rest/v1/review/customer")

public class ReviewController {
    private final JwtTokenProvider jwtTokenProvider;
    private final ReviewService reviewService;

    public ReviewController(JwtTokenProvider jwtTokenProvider, ReviewService reviewService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.reviewService = reviewService;
    }

    @GetMapping("/my-work-list")
    public ResponseEntity<?> getMyWorkList(@RequestHeader(name = "Authorization") String token) {
        try {
            List<MyWorkDTO> myWorkDTOList = reviewService.myWorkDTOList(getMyUuId(token));
            return getResponseMessage(StatusEnum.OK, "나의 작업 목록", myWorkDTOList);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.OK, nullPointerException.getMessage());
        }
    }

    @GetMapping("/my-review-list")
    public ResponseEntity<?> getMyReviewList(@RequestHeader(name = "Authorization") String token) {
        try {
            List<MyReviewDTO> myReviewDTOList = reviewService.myReviewDTOList(getMyUuId(token));
            return getResponseMessage(StatusEnum.OK, "나의 리뷰 목록", myReviewDTOList);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.toString());
        }
    }

    @PostMapping("/write")
    public ResponseEntity<?> writeReview(@RequestHeader(name = "Authorization") String token, @RequestPart(value = "images", required = false) List<MultipartFile> files
                                        , @RequestPart(value = "reviewInfo")WriteReviewDTO writeReviewDTO){
        try {
            reviewService.writeReview(files, writeReviewDTO, getMyUuId(token));
            return getResponseMessage(StatusEnum.OK, "리뷰 작성 성공", null);
        } catch (IOException e) {
            e.printStackTrace();
            return getResponseMessage(StatusEnum.OK, e.getMessage());
        }
    }

    private String getMyUuId(String token) throws NullPointerException {
        return jwtTokenProvider.getUserUuid(token.substring(7));
    }
}
