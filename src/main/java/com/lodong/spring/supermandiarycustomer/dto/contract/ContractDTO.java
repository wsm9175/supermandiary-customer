package com.lodong.spring.supermandiarycustomer.dto.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDTO {
    private String constructorId;
    private String phoneNumber;
    private String addressId;
    private String requestProductId;
    private String customerNote;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate LiveInDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate requestConstructDate;
    private boolean isConfirmationLiveIn;
    private boolean isConfirmationConstruct;
    private boolean isCashReceipt;
    private boolean cashReceiptPurpose;
    private String cashReceiptPhoneNumber;
}
