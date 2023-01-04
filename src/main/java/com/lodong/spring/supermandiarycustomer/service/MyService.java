package com.lodong.spring.supermandiarycustomer.service;

import com.lodong.spring.supermandiarycustomer.domain.usercustomer.UserCustomer;
import com.lodong.spring.supermandiarycustomer.dto.my.MyAddressListDTO;
import com.lodong.spring.supermandiarycustomer.dto.my.MyInfoDTO;
import com.lodong.spring.supermandiarycustomer.repository.UserCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyService {
    private final UserCustomerRepository userCustomerRepository;

    public MyInfoDTO getMyInfo(String uuid) {
        UserCustomer userCustomer = userCustomerRepository.findById(uuid).orElseThrow(NullPointerException::new);
        List<MyAddressListDTO> myAddressListDTOList = new ArrayList<>();
        Optional.ofNullable(userCustomer.getCustomerAddressList()).orElseGet(Collections::emptyList).forEach(customerAddress -> {
            MyAddressListDTO myAddressListDTO = null;
            if (customerAddress.getApartment() != null) {
                myAddressListDTO = new MyAddressListDTO(customerAddress.getId(), customerAddress.getApartment().getName(), customerAddress.getApartmentDong(), customerAddress.getApartmentHosu());
            } else if (customerAddress.getOtherHome() != null) {
                myAddressListDTO = new MyAddressListDTO(customerAddress.getId(), customerAddress.getOtherHome().getName(), customerAddress.getOtherHomeDong(), customerAddress.getOtherHomeHosu());
            }
            myAddressListDTOList.add(myAddressListDTO);
        });
        return new MyInfoDTO(userCustomer.getName(), myAddressListDTOList, userCustomer.getPhoneNumber());
    }
}
