package com.lodong.spring.supermandiarycustomer.fcm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushNotificationResponse {
    private int status;
    private String message;
}
