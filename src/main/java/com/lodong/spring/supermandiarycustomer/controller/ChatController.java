package com.lodong.spring.supermandiarycustomer.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.lodong.spring.supermandiarycustomer.dto.chat.*;
import com.lodong.spring.supermandiarycustomer.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiarycustomer.responseentity.StatusEnum;
import com.lodong.spring.supermandiarycustomer.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.lodong.spring.supermandiarycustomer.util.MakeResponseEntity.getResponseMessage;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("rest/v1/chat/customer")
public class ChatController {
    private final JwtTokenProvider jwtTokenProvider;
    private final ChatService chatService;

    /* @Payload
     *  보내고자 하는 데이터 자체를 의미함
     *  데이터를 전송할 때, 헤더와 메타 데이터, 에러 체크 비트 등과 같은
     *  다양한 요소들들 함께 보내어, 데이터 전송의 효율과 안정성을 높히게 된다.
     **/
    @MessageMapping("/msg")
    public void sendMessage(@Payload SendChatMessageDTO message) {
        log.info("message received : {}", message);
        try{
            ChatMessageDTO chatMessage = chatService.send(message);
            chatService.sendNotification(chatMessage);
        }catch (NullPointerException | IllegalArgumentException | FirebaseMessagingException exception){
            exception.printStackTrace();
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> newChatRoom(@RequestHeader(name = "Authorization") String token, @RequestBody ChatRoomDTO chatRoomDTO) {
        try {
            CreateChatRoomDTO createChatRoomDTO = chatService.registerNewChatRoom(getMyUuId(token), chatRoomDTO.getReceiverId());
            return getResponseMessage(StatusEnum.OK, "채팅방 생성 성공", createChatRoomDTO);
        } catch (NullPointerException nullPointerException) {
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    @PostMapping("/upload/image")
    public ResponseEntity<?> uploadImage(@RequestPart(value = "images", required = false) List<MultipartFile> files) {
        try {
            ChatFilesDTO chatFilesDTO = chatService.fileUpload(files);
            return getResponseMessage(StatusEnum.OK, "채팅 이미지 업로드 성공", chatFilesDTO);
        } catch (IOException e) {
            e.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, "파일 업로드 실패");
        }
    }


    private String getMyUuId(String token) throws NullPointerException {
        return jwtTokenProvider.getUserUuid(token.substring(7));
    }
}
