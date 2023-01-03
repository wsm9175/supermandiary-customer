package com.lodong.spring.supermandiarycustomer.controller;

import com.lodong.spring.supermandiarycustomer.domain.apart.Apartment;
import com.lodong.spring.supermandiarycustomer.domain.apart.OtherHome;
import com.lodong.spring.supermandiarycustomer.domain.usercustomer.CustomerAddress;
import com.lodong.spring.supermandiarycustomer.domain.usercustomer.CustomerPhoneNumber;
import com.lodong.spring.supermandiarycustomer.domain.usercustomer.UserCustomer;
import com.lodong.spring.supermandiarycustomer.dto.auth.CustomerAddressDTO;
import com.lodong.spring.supermandiarycustomer.dto.auth.CustomerInfoDTO;
import com.lodong.spring.supermandiarycustomer.dto.auth.LoginDTO;
import com.lodong.spring.supermandiarycustomer.dto.auth.PhoneNumberDTO;
import com.lodong.spring.supermandiarycustomer.dto.jwt.TokenRequestDTO;
import com.lodong.spring.supermandiarycustomer.enumvalue.PermissionEnum;
import com.lodong.spring.supermandiarycustomer.jwt.TokenInfo;
import com.lodong.spring.supermandiarycustomer.responseentity.StatusEnum;
import com.lodong.spring.supermandiarycustomer.service.AuthService;
import com.lodong.spring.supermandiarycustomer.service.CertifiedPhoneNumberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.lodong.spring.supermandiarycustomer.util.MakeResponseEntity.getResponseMessage;


@Slf4j
@RestController
@RequestMapping("rest/v1/auth/customer")
public class AuthController {
    private final AuthService authService;
    private final CertifiedPhoneNumberService certifiedPhoneNumberService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService, CertifiedPhoneNumberService certifiedPhoneNumberService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.certifiedPhoneNumberService = certifiedPhoneNumberService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody CustomerInfoDTO customerInfoDTO) {
        try {

            UserCustomer userCustomerForLogin = UserCustomer.builder().password(customerInfoDTO.getPw()).phoneNumber(customerInfoDTO.getPhoneNumber()).build();
            String uuid = UUID.randomUUID().toString();

            UserCustomer userCustomer = UserCustomer.builder().id(uuid).build();

            //서브 휴대폰번호 처리
            List<CustomerPhoneNumber> customerPhoneNumberList = new ArrayList<>();
            for (String s : customerInfoDTO.getSubPhoneNumberList()) {
                CustomerPhoneNumber customerPhoneNumber = CustomerPhoneNumber.builder()
                        .id(UUID.randomUUID().toString())
                        .userCustomer(userCustomer)
                        .phoneNumber(s)
                        .build();
                customerPhoneNumberList.add(customerPhoneNumber);
            }

            //주소 여러개 처리
            List<CustomerAddress> customerAddressList = new ArrayList<>();
            for (CustomerAddressDTO customerAddressDTO : customerInfoDTO.getCustomerAddressList()) {
                CustomerAddress customerAddress;
                if (customerAddressDTO.getApartmentId() != null) {
                    Apartment apartment = Apartment.builder()
                            .id(customerAddressDTO.getApartmentId())
                            .build();
                    customerAddress = CustomerAddress.builder()
                            .id(UUID.randomUUID().toString())
                            .userCustomer(userCustomer)
                            .apartment(apartment)
                            .apartmentDong(customerAddressDTO.getApartmentDong())
                            .apartmentHosu(customerAddressDTO.getApartmentHosu())
                            .build();
                    customerAddressList.add(customerAddress);
                } else if (customerAddressDTO.getOtherHomeId() != null) {
                    OtherHome otherHome = OtherHome.builder()
                            .id(customerAddressDTO.getApartmentId())
                            .build();
                    customerAddress = CustomerAddress.builder()
                            .id(UUID.randomUUID().toString())
                            .userCustomer(userCustomer)
                            .otherHome(otherHome)
                            .otherHomeDong(customerAddressDTO.getOtherHomeDong())
                            .otherHomeHosu(customerAddressDTO.getOtherHomeHosu())
                            .build();
                    customerAddressList.add(customerAddress);
                }
            }

            UserCustomer userCustomerEncode = UserCustomer.builder()
                    .id(uuid)
                    .phoneNumber(customerInfoDTO.getPhoneNumber())
                    .password(passwordEncoder.encode(customerInfoDTO.getPw()))
                    .name(customerInfoDTO.getName())
                    .email(customerInfoDTO.getEmail())
                    .sex(customerInfoDTO.getSex())
                    .interestInInterior(customerInfoDTO.isInterestInInterior())
                    .isCertification(customerInfoDTO.isCertification())
                    .isTerm(customerInfoDTO.isAgreeTerm())
                    .roles(Collections.singletonList(PermissionEnum.CUSTOMER.name()))
                    .phoneNumbers(customerPhoneNumberList)
                    .customerAddressList(customerAddressList)
                    .build();

            authService.registerCustomer(userCustomerEncode);
            TokenInfo tokenInfo = loginAfterRegister(userCustomerForLogin);
            return ResponseEntity.ok(tokenInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, "회원가입 실패 : " + e.getMessage());
        }
    }

    @PostMapping("/duplicate-check")
    public @ResponseBody boolean duplicateCheck(@RequestBody PhoneNumberDTO phoneNumber) {
        log.info("phone number received");
        return authService.isDuplicatedConstructor(phoneNumber.getPhoneNumber());
    }

    @GetMapping("/send-sms")
    public @ResponseBody String sendSMS(String phoneNumber) {
        Random rand = new Random();
        StringBuilder numstr = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numstr.append(ran);
        }
        System.out.println("수신자 번호 : " + phoneNumber);
        System.out.println("인증번호 : " + numstr);
        certifiedPhoneNumberService.certifiedPhoneNumber(phoneNumber, numstr.toString());
        return numstr.toString();
    }

    @PostMapping("/do")
    public TokenInfo auth(@RequestBody LoginDTO user) throws Exception {
        log.info("user data received {}", user);

        assert !user.getPhoneNumber().equals("");
        assert !user.getPw().equals("");
        UserCustomer userCustomer = UserCustomer.builder()
                .phoneNumber(user.getPhoneNumber())
                .password(user.getPw())
                .build();
        TokenInfo tokenInfo = authService.authCustomer(userCustomer);
        log.info(tokenInfo.toString());
        if (tokenInfo != null) {
            String refreshToken = tokenInfo.getRefreshToken();
            authService.insertRefreshTokenForCustomer(refreshToken, user.getPhoneNumber());
            return tokenInfo;
        } else throw new Exception();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> validateRefreshToken(@RequestBody TokenRequestDTO token) {
        TokenInfo tokenInfo;
        try {
            tokenInfo = authService.reissueForCustomer(token);
        } catch (RuntimeException nullPointerException) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = nullPointerException.getMessage();
            return getResponseMessage(statusEnum, message);
        }
        StatusEnum statusEnum = StatusEnum.OK;
        String message = "토근 재발급 성공";
        System.out.println(tokenInfo.toString());
        return getResponseMessage(statusEnum, message, tokenInfo);
    }



    private TokenInfo loginAfterRegister(UserCustomer userCustomer) throws Exception {
        TokenInfo tokenInfo = authService.authCustomer(userCustomer);
        log.info(tokenInfo.toString());
        if (tokenInfo != null) {
            String refreshToken = tokenInfo.getRefreshToken();
            authService.insertRefreshTokenForCustomer(refreshToken, userCustomer.getPhoneNumber());
            return tokenInfo;
        } else throw new Exception();
    }
}
