package com.lodong.spring.supermandiarycustomer.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatFilesDTO {
    private List<String> fileListId;
}
