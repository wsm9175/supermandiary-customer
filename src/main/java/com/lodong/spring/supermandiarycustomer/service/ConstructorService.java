package com.lodong.spring.supermandiarycustomer.service;

import com.lodong.spring.supermandiarycustomer.address.SiggAreas;
import com.lodong.spring.supermandiarycustomer.domain.constructor.Constructor;
import com.lodong.spring.supermandiarycustomer.domain.constructor.ConstructorFaq;
import com.lodong.spring.supermandiarycustomer.domain.constructor.ConstructorWorkArea;
import com.lodong.spring.supermandiarycustomer.domain.file.ConstructorPriceTableFile;
import com.lodong.spring.supermandiarycustomer.domain.file.ReviewImageFile;
import com.lodong.spring.supermandiarycustomer.domain.review.Review;
import com.lodong.spring.supermandiarycustomer.domain.usercustomer.CustomerAddress;
import com.lodong.spring.supermandiarycustomer.domain.usercustomer.UserCustomer;
import com.lodong.spring.supermandiarycustomer.dto.constructor.ConstructorDTO;
import com.lodong.spring.supermandiarycustomer.dto.constructor.ConstructorDetailDTO;
import com.lodong.spring.supermandiarycustomer.dto.constructor.ReviewDTO;
import com.lodong.spring.supermandiarycustomer.repository.ConstructorRepository;
import com.lodong.spring.supermandiarycustomer.repository.ConstructorWorkAreaRepository;
import com.lodong.spring.supermandiarycustomer.repository.UserCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

}
