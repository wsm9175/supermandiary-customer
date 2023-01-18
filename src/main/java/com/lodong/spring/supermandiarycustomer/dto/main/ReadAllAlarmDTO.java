package com.lodong.spring.supermandiarycustomer.dto.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadAllAlarmDTO {
    private List<String> alarmList;
}
