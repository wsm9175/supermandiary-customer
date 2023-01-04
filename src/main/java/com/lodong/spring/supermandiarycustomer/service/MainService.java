package com.lodong.spring.supermandiarycustomer.service;

import com.lodong.spring.supermandiarycustomer.address.SiggAreas;
import com.lodong.spring.supermandiarycustomer.domain.constructor.Constructor;
import com.lodong.spring.supermandiarycustomer.domain.constructor.ConstructorWorkArea;
import com.lodong.spring.supermandiarycustomer.domain.estimate.Estimate;
import com.lodong.spring.supermandiarycustomer.domain.request_order.RequestOrder;
import com.lodong.spring.supermandiarycustomer.domain.usercustomer.CustomerAddress;
import com.lodong.spring.supermandiarycustomer.domain.usercustomer.UserCustomer;
import com.lodong.spring.supermandiarycustomer.domain.working.WorkDetail;
import com.lodong.spring.supermandiarycustomer.domain.working.Working;
import com.lodong.spring.supermandiarycustomer.dto.constructor.ConstructorDTO;
import com.lodong.spring.supermandiarycustomer.dto.main.MainInfoDTO;
import com.lodong.spring.supermandiarycustomer.dto.main.MyAddressDTO;
import com.lodong.spring.supermandiarycustomer.dto.main.MyEstimateDTO;
import com.lodong.spring.supermandiarycustomer.dto.main.ScheduledWorkDTO;
import com.lodong.spring.supermandiarycustomer.enumvalue.EstimateEnum;
import com.lodong.spring.supermandiarycustomer.enumvalue.RequestOrderEnum;
import com.lodong.spring.supermandiarycustomer.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {
    private final UserCustomerRepository userCustomerRepository;
    private final WorkingRepository workingRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final ConstructorWorkAreaRepository constructorWorkAreaRepository;
    private final RequestOrderRepository requestOrderRepository;
    private final EstimateRepository estimateRepository;
    private final ConstructorRepository constructorRepository;

    @Transactional(readOnly = true)
    public List<MyAddressDTO> getMyAddressList(String uuid) {
        UserCustomer userCustomer = userCustomerRepository
                .findById(uuid)
                .orElseThrow(() -> new NullPointerException("해당 회원정보가 존재하지 않습니다"));

        List<MyAddressDTO> myAddresses = new ArrayList<>();

        for (CustomerAddress customerAddress : userCustomer.getCustomerAddressList()) {
            MyAddressDTO myAddress;
            if (customerAddress.getApartment() != null) {
                myAddress = new MyAddressDTO(customerAddress.getId(), customerAddress.getApartment().getName(), customerAddress.getApartmentDong(), customerAddress.getApartmentHosu());
            } else {
                myAddress = new MyAddressDTO(customerAddress.getId(), customerAddress.getOtherHome().getName(), customerAddress.getOtherHomeDong(), customerAddress.getOtherHomeHosu());
            }
            myAddresses.add(myAddress);
        }
        return myAddresses;
    }

    @Transactional(readOnly = true)
    public List<ConstructorDTO> getNearConstructor(String uuid, String addressId) throws NullPointerException {
        CustomerAddress customerAddress = customerAddressRepository
                .findById(addressId)
                .orElseThrow(() -> new NullPointerException("해당 주소는 존재하지 않습니다."));

        Optional<SiggAreas> siggAreas = Optional.empty();
        //주변 시공사 정보 using siggAreas
        siggAreas = Optional.ofNullable(customerAddress.getApartment().getSiggAreas());
        Set<Constructor> constructorList = new HashSet<>();

        List<Constructor> constructors = Optional.ofNullable(constructorWorkAreaRepository
                        .findBySiggAreas_Code(siggAreas.orElse(new SiggAreas()).getCode()))
                .orElseGet(Collections::emptyList)
                .stream()
                .map(ConstructorWorkArea::getConstructor)
                .toList();

        constructorList.addAll(constructors);

        List<ConstructorDTO> nearByConstructorList = new ArrayList<>();
        for (Constructor constructor : constructorList) {
            ConstructorDTO constructorDTO;
            if (constructor.getConstructorImageFile() != null) {
                constructorDTO = new ConstructorDTO(constructor.getId(), constructor.getName(), constructor.getIntroduction(), constructor.getConstructorImageFile().getFileList().getName());
            } else {
                constructorDTO = new ConstructorDTO(constructor.getId(), constructor.getName(), constructor.getIntroduction(), null);
            }
            nearByConstructorList.add(constructorDTO);
        }

        return nearByConstructorList;

    }

    @Transactional(readOnly = true)
    public MainInfoDTO getMainInfo(String uuid) throws NullPointerException {
        List<Working> workingList = new ArrayList<>();
        List<ScheduledWorkDTO> scheduledWorkDTOS = new ArrayList<>();
        List<RequestOrder> requestOrderList = new ArrayList<>();
        List<Estimate> estimateList = new ArrayList<>();
        int nowWorkingCount = 0;
        int completeWorkingCount = 0;

        //예정된 작업 목록
        //아파트인지 기타 건물인지 구분
        workingList = workingRepository
                .findByUserCustomer_Id(uuid)
                .orElse(new ArrayList<>());
        requestOrderList = requestOrderRepository.findByCustomer_Id(uuid).orElse(Collections.emptyList()).stream()
                .filter(requestOrder ->
                        requestOrder.getStatus().equals(RequestOrderEnum.BASIC.label()) || requestOrder.getStatus().equals(RequestOrderEnum.DEFER.label()))
                .toList();

        for (Working working : workingList) {
            List<WorkDetail> workDetailList = working.getWorkDetails();
            if (working.isCompletePay()) {
                completeWorkingCount++;
            } else {
                nowWorkingCount++;
            }
            for (WorkDetail workDetail : workDetailList) {
                if (workDetail.getEstimateWorkDate() != null && workDetail.getEstimateWorkTime() != null) {
                    ScheduledWorkDTO scheduledWorkDTO = new ScheduledWorkDTO(workDetail.getId(), workDetail.getEstimateWorkDate(), workDetail.getEstimateWorkTime(), working.getConstructorProduct().getName(), workDetail.getName());
                    scheduledWorkDTOS.add(scheduledWorkDTO);
                }
            }
        }
        //계약서 정보
        MyEstimateDTO myEstimateDTO = new MyEstimateDTO();
        //상태가 일반 - 보류인 계약서
        myEstimateDTO.setRequestingCount(requestOrderList.size());
        //상태가 전송인 견적서
        estimateList = estimateRepository.findByRequestOrder_Customer_Id(uuid).orElseGet(Collections::emptyList).stream()
                .filter(estimate -> estimate.getStatus().equals(EstimateEnum.SEND.label()))
                .collect(Collectors.toList());
        myEstimateDTO.setEstimateReceivedCount(estimateList.size());
        myEstimateDTO.setNowWorkingCount(nowWorkingCount);
        myEstimateDTO.setCompletedWorkingCount(completeWorkingCount);

        return new MainInfoDTO(scheduledWorkDTOS, myEstimateDTO);
    }

    @Transactional(readOnly = true)
    public List<ConstructorDTO> findConstructor(String keyword) {
        List<ConstructorDTO> constructorDTOList = new ArrayList<>();
        constructorRepository.findByNameContaining(keyword).orElseGet(Collections::emptyList).forEach(constructor -> {
            ConstructorDTO constructorDTO;
            if (constructor.getConstructorImageFile() != null) {
                constructorDTO = new ConstructorDTO(constructor.getId(), constructor.getName(), constructor.getIntroduction(), constructor.getConstructorImageFile().getFileList().getName());
            } else {
                constructorDTO = new ConstructorDTO(constructor.getId(), constructor.getName(), constructor.getIntroduction(), null);
            }
            constructorDTOList.add(constructorDTO);
        });

        return constructorDTOList;
    }
}
