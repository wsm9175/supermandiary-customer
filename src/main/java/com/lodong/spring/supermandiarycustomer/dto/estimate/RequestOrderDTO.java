package com.lodong.spring.supermandiarycustomer.dto.estimate;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data @AllArgsConstructor
public class RequestOrderDTO {
    private String customerName;
    private String phoneNumber;
    private String homeName;
    private String dong;
    private String hosu;
    private String type;
    private LocalDate livedIn;
    private boolean isConfirmationLiveIn;
    private LocalDate requestConstructorDate;
    private boolean isConfirmationConstruct;
    private boolean isCashReceipt;
    private String customerNote;
    private String productName;
    private EstimateInfoDTO estimateInfoDTO;
}
