package com.lodong.spring.supermandiarycustomer.service;

import com.lodong.spring.supermandiarycustomer.domain.constructor.Constructor;
import com.lodong.spring.supermandiarycustomer.domain.constructor.ConstructorAlarm;
import com.lodong.spring.supermandiarycustomer.domain.constructor.ConstructorProductWorkList;
import com.lodong.spring.supermandiarycustomer.domain.estimate.Estimate;
import com.lodong.spring.supermandiarycustomer.domain.request_order.RequestOrder;
import com.lodong.spring.supermandiarycustomer.domain.working.NowWorkInfo;
import com.lodong.spring.supermandiarycustomer.domain.working.WorkDetail;
import com.lodong.spring.supermandiarycustomer.domain.working.Working;
import com.lodong.spring.supermandiarycustomer.dto.estimate.*;
import com.lodong.spring.supermandiarycustomer.enumvalue.ConstructorAlarmEnum;
import com.lodong.spring.supermandiarycustomer.enumvalue.EstimateEnum;
import com.lodong.spring.supermandiarycustomer.enumvalue.RequestOrderEnum;
import com.lodong.spring.supermandiarycustomer.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EstimateService {
    private final EstimateRepository estimateRepository;
    private final RequestOrderRepository requestOrderRepository;
    private final WorkingRepository workingRepository;
    private final ConstructorAlarmRepository constructorAlarmRepository;

    @Transactional(readOnly = true)
    public List<MyRequestOrderDTO> getMyEstimateList(String uuid) throws NullPointerException {
        //List<Estimate> estimateList = estimateRepository.findByRequestOrder_Customer_Id(uuid).orElseThrow(() -> new NullPointerException("해당 회원에게 도착한 견적서가 없습니다."));
        List<RequestOrder> requestOrderList = requestOrderRepository.findByCustomer_Id(uuid).orElseThrow(() -> new NullPointerException("회원이 발송한 전자 계약서 요청건이 없습니다."));
        List<MyRequestOrderDTO> myRequestOrderDTOList = new ArrayList<>();
        for (RequestOrder requestOrder : requestOrderList) {
            MyRequestOrderDTO myRequestOrderDTO = null;
            if (requestOrder.getApartment() != null) {
                myRequestOrderDTO = new MyRequestOrderDTO(requestOrder.getId(), requestOrder.getConstructor().getName(), requestOrder.getApartment().getName(), requestOrder.getDong(), requestOrder.getHosu(), requestOrder.getApartmentType(), requestOrder.getConstructorProduct().getProduct().getName(), requestOrder.getStatus(), requestOrder.getCustomer().getName(),requestOrder.getCreateAt());
            } else if (requestOrder.getOtherHome() != null) {
                myRequestOrderDTO = new MyRequestOrderDTO(requestOrder.getId(), requestOrder.getConstructor().getName(), requestOrder.getOtherHome().getName(), requestOrder.getOtherHomeDong(), requestOrder.getOtherHomeHosu(), requestOrder.getOtherHomeType(), requestOrder.getConstructorProduct().getProduct().getName(), requestOrder.getStatus(), requestOrder.getCustomer().getName(),requestOrder.getCreateAt());
            }
            myRequestOrderDTOList.add(myRequestOrderDTO);
        }
        return myRequestOrderDTOList;
    }

    @Transactional(readOnly = true)
    public RequestOrderDTO getEstimate(String requestOrderId) throws NullPointerException {
        RequestOrder requestOrder = requestOrderRepository
                .findById(requestOrderId)
                .orElseThrow(() -> new NullPointerException("해당 계약서는 존재하지 않습니다."));

        //회원인 경우만을 고려
        String phoneNumber = requestOrder.getPhoneNumber();

        List<EstimateDetailDto> estimateDetailList = new ArrayList<>();
        List<DiscountDto> discountDtoList = new ArrayList<>();
        EstimateInfoDTO estimateInfoDTO = null;
        if (requestOrder.getEstimate() != null) {
            requestOrder.getEstimate().getEstimateDetails().forEach(estimateDetail -> {
                EstimateDetailDto estimateDetailDto = new EstimateDetailDto();
                estimateDetailDto.setName(estimateDetail.getProductName());
                estimateDetailDto.setCount(estimateDetail.getCount());
                estimateDetailDto.setPrice(estimateDetail.getPrice());
                estimateDetailList.add(estimateDetailDto);
            });
            requestOrder.getEstimate().getDiscountList().forEach(discount -> {
                DiscountDto dto = new DiscountDto();
                dto.setContent(discount.getDiscountContent());
                dto.setDiscountPrice(discount.getDiscount());
                discountDtoList.add(dto);
            });
            estimateInfoDTO = new EstimateInfoDTO(requestOrder.getEstimate().getId(),estimateDetailList, discountDtoList, requestOrder.getEstimate().getPrice(), requestOrder.getEstimate().isVat(),requestOrder.getEstimate().getNote());
        }

        RequestOrderDTO requestOrderDTO;
        if (requestOrder.getApartment() != null) {
            requestOrderDTO = new RequestOrderDTO(requestOrder.getCustomer().getName(), phoneNumber, requestOrder.getApartment().getName(), requestOrder.getDong(), requestOrder.getHosu(), requestOrder.getApartmentType(), requestOrder.getLiveInDate(), requestOrder.isConfirmationLiveIn(), requestOrder.getRequestConstructDate(), requestOrder.isConfirmationConstruct(), requestOrder.isCashReceipt(), requestOrder.getNote(), requestOrder.getConstructorProduct().getProduct().getName(), estimateInfoDTO);
        } else {
            requestOrderDTO = new RequestOrderDTO(requestOrder.getCustomer().getName(), phoneNumber, requestOrder.getOtherHome().getName(), requestOrder.getOtherHomeDong(), requestOrder.getOtherHomeHosu(), requestOrder.getOtherHomeType(), requestOrder.getLiveInDate(), requestOrder.isConfirmationLiveIn(), requestOrder.getRequestConstructDate(), requestOrder.isConfirmationConstruct(), requestOrder.isCashReceipt(), requestOrder.getNote(), requestOrder.getConstructorProduct().getProduct().getName(), estimateInfoDTO);
        }
        return requestOrderDTO;
    }

    @Transactional
    public void rejectEstimate(RejectEstimateDTO rejectEstimate) throws NullPointerException {
        Estimate estimate = estimateRepository
                .findById(rejectEstimate.getEstimateId())
                .orElseThrow(() -> new NullPointerException("존재하지 않는 견적서입니다."));
        //계약서 요청건 상태 반려로 변경
        RequestOrder requestOrder = estimate.getRequestOrder();
        requestOrder.setStatus(RequestOrderEnum.REJECT.label());
        requestOrder.setRejectMessage(rejectEstimate.getRejectMessage());
        estimate.setRequestOrder(requestOrder);
        //견적서 상태 반려로 변경
        estimate.setStatus(EstimateEnum.REJECT.label());
        estimateRepository.save(estimate);
        sendAlarm(estimate.getConstructor(), ConstructorAlarmEnum.REJECT_ESTIMATE, requestOrder.getId());
    }

    @Transactional
    public void deleteEstimate(String estimateId) {
        estimateRepository.deleteById(estimateId);
    }

    @Transactional
    public void agreeEstimate(AgreeDTO agreeDTO) {
        Estimate estimate = estimateRepository.findById(agreeDTO.getEstimateId())
                .orElseThrow(() -> new NullPointerException("존재하지 않는 견적서입니다."));
        //계약서 요청건 상태를 처리완료로 변경
        RequestOrder requestOrder = estimate.getRequestOrder();
        requestOrder.setStatus(RequestOrderEnum.PROCESSED.label());
        estimate.setRequestOrder(requestOrder);
        //견적서 상태를 처리 완료로 변경
        estimate.setStatus(EstimateEnum.PROCESSED.label());
        estimateRepository.save(estimate);

        //작업 생성
        List<ConstructorProductWorkList> constructorProductWorkLists = estimate.getConstructorProduct().getConstructorProductWorkLists();
        Set<WorkDetail> workDetails = new HashSet<>();
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

    private void sendAlarm(Constructor constructor, ConstructorAlarmEnum constructorAlarmEnum, String content) {
        ConstructorAlarm constructorAlarm = ConstructorAlarm.builder()
                .id(UUID.randomUUID().toString())
                .constructor(constructor)
                .kind(constructorAlarmEnum.toString())
                .detail(constructorAlarmEnum.label())
                .content(content)
                .createAt(LocalDateTime.now())
                .build();

        constructorAlarmRepository.save(constructorAlarm);
    }

}
