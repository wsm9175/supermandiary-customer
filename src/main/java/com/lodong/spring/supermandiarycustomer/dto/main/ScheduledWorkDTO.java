package com.lodong.spring.supermandiarycustomer.dto.main;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ScheduledWorkDTO {
    private String workDetailId;
    private LocalDate workDate;
    private LocalTime workTime;
    private String productName;
    private String workLevel;
}
