package com.lodong.spring.supermandiarycustomer.controller;


import com.lodong.spring.supermandiarycustomer.dto.contract.ContractDTO;
import com.lodong.spring.supermandiarycustomer.dto.contract.WriteContractInfoDTO;
import com.lodong.spring.supermandiarycustomer.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiarycustomer.responseentity.StatusEnum;
import com.lodong.spring.supermandiarycustomer.service.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.lodong.spring.supermandiarycustomer.util.MakeResponseEntity.getResponseMessage;

@Slf4j
@RestController
@RequestMapping("rest/v1/contract/customer")
public class ContractController {
    private final ContractService contractService;
    private final JwtTokenProvider jwtTokenProvider;

    public ContractController(ContractService contractService, JwtTokenProvider jwtTokenProvider) {
        this.contractService = contractService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/info")
    public ResponseEntity<?> getInfo(@RequestHeader(name = "Authorization") String token, String constructorId) {
        try{
            WriteContractInfoDTO writeContractInfoDTO = contractService.getInfo(getMyUuId(token), constructorId);
            return getResponseMessage(StatusEnum.OK, "견적서 작성간 필요 정보", writeContractInfoDTO);
        }catch (NullPointerException nullPointerException){
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    @PostMapping("/write")
    public ResponseEntity<?> writeContract(@RequestHeader(name = "Authorization")String token, @RequestBody ContractDTO contractDTO){
        try{
            contractService.writeContract(getMyUuId(token), contractDTO);
            return getResponseMessage(StatusEnum.OK, "전자 계약서 요청 성공", null);
        }catch (NullPointerException nullPointerException){
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.OK, nullPointerException.getMessage());

        }

    }

    private String getMyUuId(String token) throws NullPointerException {
        return jwtTokenProvider.getUserUuid(token.substring(7));
    }
}
