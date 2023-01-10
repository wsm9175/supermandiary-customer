package com.lodong.spring.supermandiarycustomer.dto.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDTO {
    private String alarmId;
    private String alarmKind;
    private String contentId;
    private String content;
}
