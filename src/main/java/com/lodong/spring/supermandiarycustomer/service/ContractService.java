package com.lodong.spring.supermandiarycustomer.service;

import com.lodong.spring.supermandiarycustomer.domain.constructor.Constructor;
import com.lodong.spring.supermandiarycustomer.domain.constructor.ConstructorAlarm;
import com.lodong.spring.supermandiarycustomer.domain.constructor.ConstructorProduct;
import com.lodong.spring.supermandiarycustomer.domain.request_order.RequestOrder;
import com.lodong.spring.supermandiarycustomer.domain.usercustomer.CustomerAddress;
import com.lodong.spring.supermandiarycustomer.domain.usercustomer.UserCustomer;
import com.lodong.spring.supermandiarycustomer.dto.contract.ConstructorProductDTO;
import com.lodong.spring.supermandiarycustomer.dto.contract.ContractDTO;
import com.lodong.spring.supermandiarycustomer.dto.contract.MyAddressDTO;
import com.lodong.spring.supermandiarycustomer.dto.contract.WriteContractInfoDTO;
import com.lodong.spring.supermandiarycustomer.enumvalue.ConstructorAlarmEnum;
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
public class ContractService {
    private final UserCustomerRepository userCustomerRepository;
    private final ConstructorRepository constructorRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final RequestOrderRepository requestOrderRepository;
    private final ConstructorProductRepository constructorProductRepository;
    private final ConstructorAlarmRepository constructorAlarmRepository;

    @Transactional(readOnly = true)
    public WriteContractInfoDTO getInfo(String uuid, String constructorId) {
        UserCustomer userCustomer = userCustomerRepository
                .findById(uuid)
                .orElseThrow(NullPointerException::new);
        Constructor constructor = constructorRepository
                .findById(constructorId)
                .orElseThrow(NullPointerException::new);

        List<String> subPhoneNumberList = new ArrayList<>();
        List<MyAddressDTO> myAddressList = new ArrayList<>();
        List<ConstructorProductDTO> constructorProductDTOS = new ArrayList<>();

        Optional.ofNullable(userCustomer.getPhoneNumbers()).orElseGet(Collections::emptyList).stream()
                .forEach(customerPhoneNumber -> {
                    subPhoneNumberList.add(customerPhoneNumber.getPhoneNumber());
                });

        Optional.ofNullable(userCustomer.getCustomerAddressList()).orElseGet(Collections::emptyList).stream()
                .forEach(customerAddress -> {
                    MyAddressDTO myAddressDTO;
                    if (customerAddress.getApartment() != null) {
                        myAddressDTO = new MyAddressDTO(customerAddress.getId(), customerAddress.getApartment().getName(), customerAddress.getApartmentDong(), customerAddress.getApartmentHosu());
                    } else {
                        myAddressDTO = new MyAddressDTO(customerAddress.getId(), customerAddress.getOtherHome().getName(), customerAddress.getOtherHomeDong(), customerAddress.getOtherHomeHosu());
                    }
                    myAddressList.add(myAddressDTO);
                });
        Optional.ofNullable(constructor.getConstructorProducts()).orElseGet(Collections::emptyList).stream()
                .forEach(constructorProduct -> {
                    ConstructorProductDTO constructorProductDTO = new ConstructorProductDTO(constructorProduct.getId(), constructorProduct.getProduct().getName());
                    constructorProductDTOS.add(constructorProductDTO);
                });

        return new WriteContractInfoDTO(userCustomer.getName(), userCustomer.getPhoneNumber(), subPhoneNumberList, myAddressList, constructorProductDTOS);
    }

    @Transactional
    public void writeContract(String uuid, ContractDTO contractDTO) throws NullPointerException {
        UserCustomer userCustomer = userCustomerRepository
                .findById(uuid)
                .orElseThrow(() -> new NullPointerException("해당 유저는 존재하지 않는 유저입니다."));
        Constructor constructor = constructorRepository
                .findById(contractDTO.getConstructorId())
                .orElseThrow(() -> new NullPointerException("해당 시공사는 존재하지 않는 시공사 입니다."));
        CustomerAddress customerAddress = customerAddressRepository
                .findById(contractDTO.getAddressId())
                .orElseThrow(() -> new NullPointerException("해당 주소는 존재하지 않는 주소입니다."));
        ConstructorProduct constructorProduct = constructorProductRepository
                .findById(contractDTO.getRequestProductId())
                .orElseThrow(()-> new NullPointerException("해당 물건은 존재하지 않습니다."));

        RequestOrder requestOrder = null;
        String requestOrderId = UUID.randomUUID().toString();
        if (customerAddress.getApartment() != null) {
            requestOrder = RequestOrder.builder()
                    .id(requestOrderId)
                    .constructor(constructor)
                    .customer(userCustomer)
                    .apartment(customerAddress.getApartment())
                    .apartmentType(customerAddress.getType())
                    .dong(customerAddress.getApartmentDong())
                    .hosu(customerAddress.getApartmentHosu())
                    .note(contractDTO.getCustomerNote())
                    .status(RequestOrderEnum.BASIC.label())
                    .liveInDate(contractDTO.getLiveInDate())
                    .requestConstructDate(contractDTO.getRequestConstructDate())
                    .isConfirmationLiveIn(contractDTO.isConfirmationConstruct())
                    .isConfirmationConstruct(contractDTO.isConfirmationConstruct())
                    .isCashReceipt(contractDTO.isCashReceipt())
                    .cashReceiptPurpose(contractDTO.isCashReceiptPurpose())
                    .cashReceiptPhoneNumber(contractDTO.getCashReceiptPhoneNumber())
                    .phoneNumber(contractDTO.getPhoneNumber())
                    .createAt(LocalDateTime.now())
                    .constructorProduct(constructorProduct)
                    .build();

        } else if (customerAddress.getOtherHome() != null) {
            requestOrder = RequestOrder.builder()
                    .id(requestOrderId)
                    .constructor(constructor)
                    .customer(userCustomer)
                    .otherHome(customerAddress.getOtherHome())
                    .otherHomeType(customerAddress.getType())
                    .otherHomeDong(customerAddress.getOtherHomeDong())
                    .otherHomeHosu(customerAddress.getOtherHomeHosu())
                    .note(contractDTO.getCustomerNote())
                    .status(RequestOrderEnum.BASIC.label())
                    .liveInDate(contractDTO.getLiveInDate())
                    .requestConstructDate(contractDTO.getRequestConstructDate())
                    .isConfirmationLiveIn(contractDTO.isConfirmationConstruct())
                    .isConfirmationConstruct(contractDTO.isConfirmationConstruct())
                    .isCashReceipt(contractDTO.isCashReceipt())
                    .cashReceiptPurpose(contractDTO.isCashReceiptPurpose())
                    .cashReceiptPhoneNumber(contractDTO.getCashReceiptPhoneNumber())
                    .phoneNumber(contractDTO.getPhoneNumber())
                    .createAt(LocalDateTime.now())
                    .constructorProduct(constructorProduct)
                    .build();
        }
        requestOrderRepository.save(requestOrder);

        sendAlarm(constructor, ConstructorAlarmEnum.RECEIVE_REQUEST_ORDER, requestOrderId);
    }

    private void sendAlarm(Constructor constructor,ConstructorAlarmEnum constructorAlarmEnum, String content){
        ConstructorAlarm constructorAlarm = ConstructorAlarm.builder()
                .id(UUID.randomUUID().toString())
                .constructor(constructor)
                .kind(constructorAlarmEnum.label())
                .detail(constructorAlarmEnum.label())
                .content(content)
                .createAt(LocalDateTime.now())
                .build();

        constructorAlarmRepository.save(constructorAlarm);
    }
}
