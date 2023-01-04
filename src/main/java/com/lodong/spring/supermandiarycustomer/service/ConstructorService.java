package com.lodong.spring.supermandiarycustomer.service;

import com.lodong.spring.supermandiarycustomer.address.SiggAreas;
import com.lodong.spring.supermandiarycustomer.domain.constructor.Constructor;
import com.lodong.spring.supermandiarycustomer.domain.constructor.ConstructorFaq;
import com.lodong.spring.supermandiarycustomer.domain.constructor.ConstructorWorkArea;
import com.lodong.spring.supermandiarycustomer.domain.file.ConstructorPriceTableFile;
import com.lodong.spring.supermandiarycustomer.domain.file.ReviewImageFile;
import com.lodong.spring.supermandiarycustomer.domain.review.Review;
import com.lodong.spring.supermandiarycustomer.domain.review.ReviewComment;
import com.lodong.spring.supermandiarycustomer.domain.usercustomer.CustomerAddress;
import com.lodong.spring.supermandiarycustomer.domain.usercustomer.UserCustomer;
import com.lodong.spring.supermandiarycustomer.dto.constructor.*;
import com.lodong.spring.supermandiarycustomer.repository.ConstructorRepository;
import com.lodong.spring.supermandiarycustomer.repository.ConstructorWorkAreaRepository;
import com.lodong.spring.supermandiarycustomer.repository.ReviewRepository;
import com.lodong.spring.supermandiarycustomer.repository.UserCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConstructorService {
    private final UserCustomerRepository userCustomerRepository;
    private final ConstructorRepository constructorRepository;
    private final ConstructorWorkAreaRepository constructorWorkAreaRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public List<ConstructorDTO> getConstructor(String customerId) {
        //로그인한 customer의 지역값을 받아온다.
        UserCustomer userCustomer = userCustomerRepository
                .findById(customerId)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 고객입니다."));

        List<SiggAreas> customerSiggList = new ArrayList<>();
        for (CustomerAddress customerAddress : userCustomer.getCustomerAddressList()) {
            if (customerAddress.getApartment() != null) {
                customerSiggList.add(customerAddress.getApartment().getSiggAreas());
            } else if (customerAddress.getOtherHome() != null) {
                customerSiggList.add(customerAddress.getOtherHome().getSiggAreas());
            }
        }
        Set<Constructor> constructorList = new HashSet<>();
        for (SiggAreas siggAreas : customerSiggList) {
            List<Constructor> constructors = Optional.ofNullable(constructorWorkAreaRepository
                            .findBySiggAreas_Code(siggAreas.getCode()))
                    .orElseGet(Collections::emptyList)
                    .stream()
                    .map(ConstructorWorkArea::getConstructor)
                    .toList();

            constructorList.addAll(constructors);
        }

        List<ConstructorDTO> constructorDTOS = new ArrayList<>();
        for (Constructor constructor : constructorList) {
            ConstructorDTO constructorDTO = new ConstructorDTO(constructor.getId(), constructor.getName(), constructor.getIntroduction(), constructor.getConstructorImageFile().getFileList().getName());
            constructorDTOS.add(constructorDTO);
        }
        return constructorDTOS;
    }
    @Transactional(readOnly = true)
    public ConstructorDetailDTO getConstructorDetail(String constructorId) {
        Constructor constructor = constructorRepository.findById(constructorId).orElseThrow(() -> new NullPointerException("해당 시공사는 존재하지 않습니다."));

        //작업 가능 구역
        List<String> possibleAreas = new ArrayList<>();
        for (ConstructorWorkArea constructorWorkArea : constructor.getConstructorWorkAreas()) {
            String possibleArea = constructorWorkArea.getSiggAreas().getSidoAreas().getName() + " " + constructorWorkArea.getSiggAreas().getName();
            possibleAreas.add(possibleArea);
        }
        //단가표 목록
        List<String> priceTableList = new ArrayList<>();
        for (ConstructorPriceTableFile constructorPriceTableFile : constructor.getConstructorPriceTableFiles()) {
            priceTableList.add(constructorPriceTableFile.getFileList().getName());
        }
        //리뷰 목록
        List<ReviewDTO> reviewDTOList = new ArrayList<>();
        for (Review review : constructor.getReviewList()) {
            //review image 목록
            List<String> reviewImageFileNameList = new ArrayList<>();
            for (ReviewImageFile reviewImageFile : review.getReviewImageFileList()) {
                reviewImageFileNameList.add(reviewImageFile.getFileList().getName());
            }
            ReviewDTO reviewDTO = new ReviewDTO(review.getId(), reviewImageFileNameList, review.getCustomer().getId(), review.getCustomerName(), review.isSatisfaction(), review.getContents());
            reviewDTOList.add(reviewDTO);
        }
        //faq 목록
        List<String> questionList = new ArrayList<>();
        for (ConstructorFaq constructorFaq : constructor.getConstructorFaqlist()) {
            questionList.add(constructorFaq.getQuestion());
        }
        ConstructorDetailDTO constructorDetailDTO = new ConstructorDetailDTO(constructor.getName(), possibleAreas, constructor.getIntroduction(),
                priceTableList, reviewDTOList, questionList);

        return constructorDetailDTO;
    }

    @Transactional(readOnly = true)
    public ReviewPageDTO getReviewPageInfo(String constructorId){
        PageRequest pageRequest = PageRequest.of(0, 15, Sort.by("createAt").descending());
        Page<Review> reviewPage = reviewRepository.findByConstructor_Id(constructorId, pageRequest);
        List<Review> reviewList = Optional.of(reviewPage.getContent()).orElseGet(Collections::emptyList);
        List<String> fileNames = new ArrayList<>();
        int totalPages = reviewPage.getTotalPages();
        boolean hasNextPage = reviewPage.hasNext();
        long totalCount = reviewPage.getTotalElements();
        List<ReviewDetailDTO> reviewDetailDTOS = new ArrayList<>();

        for(Review review : reviewList){
            List<String> likeUserIdList = new ArrayList<>();
            List<String> fileNameList = new ArrayList<>();
            ReviewDetailDTO reviewDetailDTO = new ReviewDetailDTO();
            reviewDetailDTO.setReviewId(review.getId());
            reviewDetailDTO.setWriterId(review.getCustomer().getId());
            reviewDetailDTO.setWriterName(review.getCustomerName());
            reviewDetailDTO.setCreateAt(review.getCreateAt());
            reviewDetailDTO.setLikeCount(Optional.ofNullable(review.getReviewLikeList()).orElseGet(Collections::emptySet).size());
            Optional.ofNullable(review.getReviewLikeList()).orElseGet(Collections::emptySet).forEach(reviewLike -> {
                likeUserIdList.add(reviewLike.getUserCustomer().getId());
            });
            reviewDetailDTO.setLikeUserIdList(likeUserIdList);
            Optional.ofNullable(review.getReviewImageFileList()).orElseGet(Collections::emptySet).forEach(reviewImageFile -> {
                fileNames.add(reviewImageFile.getFileList().getName());
                fileNameList.add(reviewImageFile.getFileList().getName());
            });
            reviewDetailDTO.setFileNameList(fileNameList);
            log.info("id : "+review.getId());
            Optional.ofNullable(review.getReviewComment()).ifPresent(reviewComment -> {
                ReviewCommentDTO reviewCommentDTO = new ReviewCommentDTO(reviewComment.getId(), reviewComment.getConstructorName(), reviewComment.getCreateAt(), reviewComment.getContents());
                reviewDetailDTO.setReviewComment(reviewCommentDTO);
            });
            reviewDetailDTO.setProductName(review.getWorking().getConstructorProduct().getName());
            reviewDetailDTO.setContents(review.getContents());
            reviewDetailDTOS.add(reviewDetailDTO);
        }

        return new ReviewPageDTO(totalCount, totalPages, hasNextPage, fileNames, reviewDetailDTOS);
    }

    @Transactional(readOnly = true)
    public ReviewPagingDTO getReviewPage(String constructorId, int page){
        PageRequest pageRequest = PageRequest.of(page, 15, Sort.by("createAt").descending());
        Page<Review> reviewPage = reviewRepository.findByConstructor_Id(constructorId, pageRequest);
        List<Review> reviewList = Optional.of(reviewPage.getContent()).orElseGet(Collections::emptyList);
        int totalPages = reviewPage.getTotalPages();
        boolean hasNextPage = reviewPage.hasNext();
        List<ReviewDetailDTO> reviewDetailDTOS = new ArrayList<>();

        for(Review review : reviewList){
            List<String> likeUserIdList = new ArrayList<>();
            List<String> fileNameList = new ArrayList<>();
            ReviewDetailDTO reviewDetailDTO = new ReviewDetailDTO();
            reviewDetailDTO.setReviewId(review.getId());
            reviewDetailDTO.setWriterId(review.getCustomer().getId());
            reviewDetailDTO.setWriterName(review.getCustomerName());
            reviewDetailDTO.setCreateAt(review.getCreateAt());
            reviewDetailDTO.setLikeCount(Optional.ofNullable(review.getReviewLikeList()).orElseGet(Collections::emptySet).size());
            Optional.ofNullable(review.getReviewLikeList()).orElseGet(Collections::emptySet).forEach(reviewLike -> {
                likeUserIdList.add(reviewLike.getUserCustomer().getId());
            });
            reviewDetailDTO.setLikeUserIdList(likeUserIdList);
            Optional.ofNullable(review.getReviewImageFileList()).orElseGet(Collections::emptySet).forEach(reviewImageFile -> {
                fileNameList.add(reviewImageFile.getFileList().getName());
            });
            reviewDetailDTO.setFileNameList(fileNameList);
            Optional.ofNullable(review.getReviewComment()).ifPresent(reviewComment -> {
                ReviewCommentDTO reviewCommentDTO = new ReviewCommentDTO(reviewComment.getId(), reviewComment.getConstructorName(), reviewComment.getCreateAt(), reviewComment.getContents());
                reviewDetailDTO.setReviewComment(reviewCommentDTO);
            });
            reviewDetailDTO.setProductName(review.getWorking().getConstructorProduct().getName());
            reviewDetailDTO.setContents(review.getContents());
            reviewDetailDTOS.add(reviewDetailDTO);
        }

        return new ReviewPagingDTO(totalPages, hasNextPage, reviewDetailDTOS);
    }



}
