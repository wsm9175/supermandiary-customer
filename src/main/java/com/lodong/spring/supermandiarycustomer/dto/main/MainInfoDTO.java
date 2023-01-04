package com.lodong.spring.supermandiarycustomer.dto.main;

import com.lodong.spring.supermandiarycustomer.dto.constructor.ConstructorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainInfoDTO {
    private List<ScheduledWorkDTO> scheduledWorkList;
    private MyEstimateDTO myEstimateDTO;
}
