package com.lodong.spring.supermandiarycustomer.util;


import com.lodong.spring.supermandiarycustomer.responseentity.Message;
import com.lodong.spring.supermandiarycustomer.responseentity.StatusEnum;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.Charset;

public class MakeResponseEntity {
    public static ResponseEntity getResponseMessage(StatusEnum status, String message, Object data) {
        Message responseMessage = new Message(status, message, data);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    public static ResponseEntity getResponseMessage(StatusEnum status, String message) {
        Message responseMessage = new Message(status, message);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_REQUEST);
    }
}
