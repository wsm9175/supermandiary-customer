package com.lodong.spring.supermandiarycustomer.dto.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyEstimateDTO {
    private int requestingCount;
    private int estimateReceivedCount;
    private int nowWorkingCount;
    private int completedWorkingCount;
}
