package com.lodong.spring.supermandiarycustomer.service;

import com.lodong.spring.supermandiarycustomer.domain.apart.Apartment;
import com.lodong.spring.supermandiarycustomer.domain.constructor.ConstructorProductWorkList;
import com.lodong.spring.supermandiarycustomer.domain.estimate.Estimate;
import com.lodong.spring.supermandiarycustomer.domain.request_order.RequestOrder;
import com.lodong.spring.supermandiarycustomer.domain.usercustomer.UserCustomer;
import com.lodong.spring.supermandiarycustomer.domain.working.NowWorkInfo;
import com.lodong.spring.supermandiarycustomer.domain.working.WorkDetail;
import com.lodong.spring.supermandiarycustomer.domain.working.Working;
import com.lodong.spring.supermandiarycustomer.dto.estimate.*;
import com.lodong.spring.supermandiarycustomer.enumvalue.EstimateEnum;
import com.lodong.spring.supermandiarycustomer.enumvalue.RequestOrderEnum;
import com.lodong.spring.supermandiarycustomer.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EstimateService {
    private final EstimateRepository estimateRepository;
    private final RequestOrderRepository requestOrderRepository;
    private final UserCustomerRepository userCustomerRepository;
    private final NowWorkInfoRepository nowWorkInfoRepository;
    private final WorkDetailRepository workDetailRepository;
    private final WorkingRepository workingRepository;

    @Transactional(readOnly = true)
    public List<MyEstimateDTO> getMyEstimateList(String uuid) throws NullPointerException {
        List<Estimate> estimateList = estimateRepository.findByRequestOrder_Customer_Id(uuid).orElseThrow(() -> new NullPointerException("해당 회원에게 도착한 견적서가 없습니다."));
        List<MyEstimateDTO> myEstimateDTOList = new ArrayList<>();
        for (Estimate estimate : estimateList) {
            MyEstimateDTO myEstimateDTO = null;
            if (estimate.getRequestOrder().getApartment() != null) {
                myEstimateDTO = new MyEstimateDTO(estimate.getId(), estimate.getConstructor().getName(), estimate.getRequestOrder().getApartment().getName(), estimate.getRequestOrder().getDong(), estimate.getRequestOrder().getHosu(), estimate.getRequestOrder().getApartment_type(), estimate.getConstructorProduct().getName(), estimate.getStatus());
            } else if (estimate.getRequestOrder().getOtherHome() != null) {
                myEstimateDTO = new MyEstimateDTO(estimate.getId(), estimate.getConstructor().getName(), estimate.getRequestOrder().getOtherHome().getName(), estimate.getRequestOrder().getOtherHomeDong(), estimate.getRequestOrder().getOtherHomeHosu(), estimate.getRequestOrder().getOtherHomeType(), estimate.getConstructorProduct().getName(), estimate.getStatus());
            }
            myEstimateDTOList.add(myEstimateDTO);
        }
        return myEstimateDTOList;
    }

    @Transactional(readOnly = true)
    public EstimateInfoDto getEstimate(String estimateId) throws NullPointerException {
        Estimate estimate = estimateRepository
                .findById(estimateId)
                .orElseThrow(() -> new NullPointerException("해당 견적서는 존재하지 않습니다."));

        //회원인 경우만을 고려
        List<String> phoneNumber = new ArrayList<>();
        estimate.getRequestOrder().getCustomer().getPhoneNumbers().forEach(customerPhoneNumber -> {
            phoneNumber.add(customerPhoneNumber.getPhoneNumber());
        });
        List<EstimateDetailDto> estimateDetailList = new ArrayList<>();
        estimate.getEstimateDetails().forEach(estimateDetail -> {
            EstimateDetailDto estimateDetailDto = new EstimateDetailDto();
            estimateDetailDto.setName(estimateDetail.getProductName());
            estimateDetailDto.setCount(estimateDetail.getCount());
            estimateDetailDto.setPrice(estimateDetail.getPrice());
            estimateDetailList.add(estimateDetailDto);
        });
        List<DiscountDto> discountDtoList = new ArrayList<>();
        estimate.getDiscountList().stream().forEach(discount -> {
            DiscountDto dto = new DiscountDto();
            dto.setContent(discount.getDiscountContent());
            dto.setDiscountPrice(discount.getDiscount());
            discountDtoList.add(dto);
        });

        EstimateInfoDto estimateInfoDto;
        if (estimate.getRequestOrder().getApartment() != null) {
            estimateInfoDto = new EstimateInfoDto(estimate.getConstructor().getName(), phoneNumber, estimate.getRequestOrder().getApartment().getName(), estimate.getRequestOrder().getDong(), estimate.getRequestOrder().getHosu(), estimate.getRequestOrder().getApartment_type(), estimate.getRequestOrder().getLiveInDate(), estimate.getRequestOrder().isConfirmationLiveIn(), estimate.getRequestOrder().getRequestConstructDate(), estimate.getRequestOrder().isConfirmationConstruct(), estimate.getRequestOrder().isCashReceipt(), estimate.getRequestOrder().getNote(), estimate.getConstructorProduct().getName(), estimateDetailList, discountDtoList, estimate.getPrice(), estimate.isVat(), estimate.getNote());
        } else {
            estimateInfoDto = new EstimateInfoDto(estimate.getConstructor().getName(), phoneNumber, estimate.getRequestOrder().getOtherHome().getName(), estimate.getRequestOrder().getOtherHomeDong(), estimate.getRequestOrder().getOtherHomeHosu(), estimate.getRequestOrder().getOtherHomeType(), estimate.getRequestOrder().getLiveInDate(), estimate.getRequestOrder().isConfirmationLiveIn(), estimate.getRequestOrder().getRequestConstructDate(), estimate.getRequestOrder().isConfirmationConstruct(), estimate.getRequestOrder().isCashReceipt(), estimate.getRequestOrder().getNote(), estimate.getConstructorProduct().getName(), estimateDetailList, discountDtoList, estimate.getPrice(), estimate.isVat(), estimate.getNote());
        }
        return estimateInfoDto;
    }

    @Transactional
    public void rejectEstimate(RejectEstimateDTO rejectEstimate) throws NullPointerException{
        Estimate estimate = estimateRepository
                .findById(rejectEstimate.getEstimateId())
                .orElseThrow(()->new NullPointerException("존재하지 않는 견적서입니다."));
        //계약서 요청건 상태 반려로 변경
        RequestOrder requestOrder = estimate.getRequestOrder();
        requestOrder.setStatus(RequestOrderEnum.REJECT.label());
        requestOrder.setRejectMessage(rejectEstimate.getRejectMessage());
        estimate.setRequestOrder(requestOrder);
        //견적서 상태 반려로 변경
        estimate.setStatus(EstimateEnum.REJECT.label());
        estimateRepository.save(estimate);
    }

    @Transactional
    public void deleteEstimate(String estimateId){
        estimateRepository.deleteById(estimateId);
    }

    @Transactional
    public void agreeEstimate(AgreeDTO agreeDTO){
        Estimate estimate = estimateRepository.findById(agreeDTO.getEstimateId())
                .orElseThrow(()->new NullPointerException("존재하지 않는 견적서입니다."));
        //계약서 요청건 상태를 처리완료로 변경
        RequestOrder requestOrder = estimate.getRequestOrder();
        requestOrder.setStatus(RequestOrderEnum.PROCESSED.label());
        estimate.setRequestOrder(requestOrder);
        //견적서 상태를 처리 완료로 변경
        estimate.setStatus(EstimateEnum.PROCESSED.label());
        estimateRepository.save(estimate);

        //작업 생성
        List<ConstructorProductWorkList> constructorProductWorkLists = estimate.getConstructorProduct().getConstructorProductWorkLists();
        List<WorkDetail> workDetails = new ArrayList<>();
        Working working = null;
        if (estimate.getRequestOrder().getApartment() != null) {
            working = Working.builder()
                    .id(UUID.randomUUID().toString())
                    .constructor(estimate.getConstructor())
                    .constructorProduct(estimate.getConstructorProduct())
                    .estimate(estimate)
                    .completeConstruct(false)
                    .completePay(false)
                    .apartment(estimate.getRequestOrder().getApartment())
                    .apartmentDong(estimate.getRequestOrder().getDong())
                    .apartmentHosu(estimate.getRequestOrder().getHosu())
                    .apartmentType(estimate.getRequestOrder().getOtherHomeType())
                    .userCustomer(estimate.getRequestOrder().getCustomer())
                    .build();

        } else if (estimate.getRequestOrder().getOtherHome() != null) {
            working = Working.builder()
                    .id(UUID.randomUUID().toString())
                    .constructor(estimate.getConstructor())
                    .constructorProduct(estimate.getConstructorProduct())
                    .estimate(estimate)
                    .completeConstruct(false)
                    .completePay(false)
                    .otherHome(estimate.getRequestOrder().getOtherHome())
                    .otherHomeDong(estimate.getRequestOrder().getOtherHomeDong())
                    .otherHomeHosu(estimate.getRequestOrder().getOtherHomeHosu())
                    .otherHomeType(estimate.getRequestOrder().getOtherHomeType())
                    .userCustomer(estimate.getRequestOrder().getCustomer())
                    .build();
        }
        for (ConstructorProductWorkList constructorProductWorkList : constructorProductWorkLists) {
            WorkDetail workDetail = WorkDetail.builder()
                    .id(UUID.randomUUID().toString())
                    .working(working)
                    .name(constructorProductWorkList.getName())
                    .sequence(constructorProductWorkList.getSequence())
                    .isFileIn(constructorProductWorkList.isFileIn())
                    .isComplete(false).build();
            workDetails.add(workDetail);
        }
        WorkDetail firstWorkDetail = null;
        for (WorkDetail workDetail : workDetails) {
            if (workDetail.getSequence() == 1) {
                firstWorkDetail = workDetail;
                break;
            }
        }

        NowWorkInfo nowWorkInfo = NowWorkInfo.builder().id(UUID.randomUUID().toString()).working(working).workDetail(firstWorkDetail).build();

        working.setWorkDetails(workDetails);
        working.setNowWorkInfo(nowWorkInfo);
        workingRepository.save(working);
        //workDetailRepository.saveAll(workDetails);
        //nowWorkInfoRepository.save(nowWorkInfo);
    }

}
